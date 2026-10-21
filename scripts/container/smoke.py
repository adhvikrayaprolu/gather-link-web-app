"""Exercise the packaged JSP WAR with real HTTP, cookies and CSRF; stdlib only."""
from html.parser import HTMLParser
from http.cookiejar import CookieJar
import os
import re
import sys
from urllib.error import HTTPError
from urllib.parse import urlencode
from urllib.request import HTTPCookieProcessor, HTTPRedirectHandler, Request, build_opener

BASE = os.environ.get("SMOKE_APP_URL", "")
PROJECT = os.environ.get("SMOKE_DISPOSABLE_PROJECT", "")
if BASE != "http://app:8080" or not re.fullmatch(r"gather-check-\d+-\d+", PROJECT):
    raise SystemExit("Refusing smoke writes outside the disposable local check project. Run make check.")
PASSWORD = "container-fixture-password-234"
GROUP = "Container verification group"
OWNER_POST = "Owner post from packaged WAR verification"
MEMBER_POST = "Member post from packaged WAR verification"


class NoRedirects(HTTPRedirectHandler):
    def redirect_request(self, request, fp, code, message, headers, new_url):
        return None


class Inputs(HTMLParser):
    def __init__(self):
        super().__init__()
        self.csrf = None
        self.group_id = None

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        if tag == "input" and attrs.get("name") == "_csrf":
            self.csrf = attrs.get("value")
        if tag == "form":
            match = re.fullmatch(r"/groups/(\d+)/join", attrs.get("action", ""))
            if match:
                self.group_id = match.group(1)


class Browser:
    def __init__(self):
        self.client = build_opener(HTTPCookieProcessor(CookieJar()), NoRedirects())

    def request(self, path, fields=None, expected=200):
        data = urlencode(fields).encode() if fields is not None else None
        request = Request(BASE + path, data=data)
        try:
            response = self.client.open(request, timeout=12)
        except HTTPError as error:
            response = error
        body = response.read().decode("utf-8")
        if response.code != expected:
            # Response bodies can contain private information; log only the route/status.
            raise AssertionError(f"{request.method} {path}: expected {expected}, received {response.code}")
        return body, response.headers

    def page(self, path):
        body, _ = self.request(path)
        assert "<%@" not in body, "JSP source must not be served uncompiled"
        return body

    def submit(self, page, target, fields, location):
        inputs = Inputs(); inputs.feed(self.page(page))
        assert inputs.csrf, f"Missing CSRF form on {page}"
        _, headers = self.request(target, fields | {"_csrf": inputs.csrf}, expected=302)
        assert headers["Location"].endswith(location), f"Unexpected redirect from {target}"

    def login(self, username):
        self.submit("/login", "/login", {"username": username, "password": PASSWORD}, "/home")
        assert "Welcome to GatherLink" in self.page("/home")

    def register(self, username):
        self.submit("/register", "/register", {"username": username, "email": username + "@example.test", "password": PASSWORD}, "/login?registered")
        self.login(username)


def group_id(browser):
    body = browser.page("/explore-groups")
    assert GROUP in body
    inputs = Inputs(); inputs.feed(body)
    assert inputs.group_id
    return inputs.group_id


def seed():
    anonymous = Browser()
    _, headers = anonymous.request("/my-groups", expected=302)
    assert headers["Location"].endswith("/login")
    anonymous.request("/register", {"username": "must_not_exist", "email": "denied@example.test", "password": PASSWORD}, expected=403)
    owner = Browser(); owner.register("container_owner")
    owner.submit("/groups/create", "/groups/add", {"groupName": GROUP, "description": "Synthetic disposable container verification."}, "/explore-groups")
    group = group_id(owner)
    owner.submit("/posts/create?groupId="+group, "/posts/create", {"groupId": group, "content": OWNER_POST}, "/posts/group/"+group)
    assert OWNER_POST in owner.page("/posts/group/"+group)
    member = Browser(); member.register("container_member")
    member.request("/posts/group/"+group, expected=403)
    for _ in range(2):
        member.submit("/explore-groups", "/groups/"+group+"/join", {}, "/explore-groups")
    assert "Members: 1" in member.page("/explore-groups"), "Repeated joins must not duplicate memberships"
    member.submit("/posts/create?groupId="+group, "/posts/create", {"groupId": group, "content": MEMBER_POST}, "/posts/group/"+group)
    assert OWNER_POST in member.page("/posts/group/"+group)
    assert MEMBER_POST in member.page("/posts/group/"+group)
    print("Packaged JSP, registration/session, CSRF, group/post authorization and idempotent joins passed.")


def persistence():
    for username in ["container_owner", "container_member"]:
        browser = Browser(); browser.login(username)
        group = group_id(browser)
        body = browser.page("/posts/group/"+group)
        assert OWNER_POST in body and MEMBER_POST in body
        assert "Members: 1" in browser.page("/explore-groups")
        browser.submit("/home", "/logout", {}, "/login?logout")
        browser.request("/my-groups", expected=302)
    print("Fresh sessions after container replacement: accounts, group, membership and posts persisted; logout passed.")


if len(sys.argv) != 2 or sys.argv[1] not in {"seed", "persistence"}:
    raise SystemExit("Choose the seed or persistence phase through make check.")
{"seed": seed, "persistence": persistence}[sys.argv[1]]()
