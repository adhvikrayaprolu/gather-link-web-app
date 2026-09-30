package com.gather_link;
import com.gather_link.model.*;
import com.gather_link.repository.*;
import com.gather_link.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:flow;MODE=MySQL;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional
class EngineeringFlowTests {
 @Autowired MockMvc mvc; @Autowired UserService users; @Autowired PasswordEncoder encoder;
 @Autowired GroupService groups; @Autowired GroupMembershipService memberships; @Autowired PostService posts;
 @Autowired GroupMembershipRepository membershipRepo; @Autowired GroupRepository groupRepo; @Autowired PostRepository postRepo;
 Users owner, member, stranger; Groups group;
 Users account(String name){var u=new Users();u.setUsername(name);u.setEmail(name+"@example.test");u.setPassword("test-password-234");users.create(u);return u;}
 MockHttpSession session(Users user){var s=new MockHttpSession();s.setAttribute("loggedInUser",user);return s;}
 @BeforeEach void setup(){owner=account("owner");member=account("member");stranger=account("stranger");group=new Groups();group.setGroupName("Engineering");group.setDescription("Build useful things");group.setOwner(owner);groups.create(group);}
 @Test void hashesPasswordsAndDoesNotSerializeThem()throws Exception{
  assertNotEquals("test-password-234",owner.getPassword());assertTrue(encoder.matches("test-password-234",owner.getPassword()));
  assertFalse(new com.fasterxml.jackson.databind.ObjectMapper().findAndRegisterModules().writeValueAsString(owner).contains("password"));
 }
 @Test void rejectsWeakDuplicateAndInvalidAccounts(){assertThrows(IllegalArgumentException.class,()->account("owner"));var u=new Users();u.setUsername("valid");u.setEmail("valid@example.test");u.setPassword("short");assertThrows(IllegalArgumentException.class,()->users.create(u));}
 @Test void anonymousPagesRequireAuthentication()throws Exception{mvc.perform(get("/my-groups")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));}
 @Test void csrfRequiredEvenForRegistration()throws Exception{mvc.perform(post("/register").param("username","newuser").param("email","new@example.test").param("password","test-password-234")).andExpect(status().isForbidden());}
 @Test void registrationAndRealLoginWork()throws Exception{
  mvc.perform(post("/register").with(csrf()).param("username","newuser").param("email","new@example.test").param("password","test-password-234")).andExpect(redirectedUrl("/login?registered"));
  mvc.perform(post("/login").with(csrf()).param("username","newuser").param("password","test-password-234")).andExpect(redirectedUrl("/home")).andExpect(request().sessionAttribute("loggedInUser",org.hamcrest.Matchers.instanceOf(Users.class)));
  mvc.perform(post("/login").with(csrf()).param("username","newuser").param("password","wrong")).andExpect(redirectedUrl("/login?error"));
 }
 @Test void joinsAreIdempotentAndCountReflectsMembership(){memberships.addMember(group,member);memberships.addMember(group,member);assertEquals(1,membershipRepo.countByGroup(group));assertEquals(1,groupRepo.findById(group.getGroupId()).orElseThrow().getMemberCount());}
 @Test void outsidersCannotReadOrCreateGroupPosts()throws Exception{
  mvc.perform(get("/posts/group/"+group.getGroupId()).with(user("stranger")).session(session(stranger))).andExpect(status().isForbidden());
  mvc.perform(post("/posts/create").with(user("stranger")).with(csrf()).session(session(stranger)).param("groupId",group.getGroupId().toString()).param("content","Not allowed")).andExpect(status().isForbidden());assertEquals(0,postRepo.count());
 }
 @Test void memberCanPostButCannotChangeRoles()throws Exception{
  memberships.addMember(group,member);
  mvc.perform(post("/posts/create").with(user("member")).with(csrf()).session(session(member)).param("groupId",group.getGroupId().toString()).param("content","A useful post")).andExpect(redirectedUrl("/posts/group/"+group.getGroupId()));
  assertEquals(1,postRepo.countByGroup(group));assertEquals(1,groupRepo.findById(group.getGroupId()).orElseThrow().getPostCount());
  mvc.perform(post("/groups/"+group.getGroupId()+"/members/"+member.getUserId()+"/role").with(user("member")).with(csrf()).session(session(member)).param("role","ADMIN")).andExpect(status().isForbidden());
 }
 @Test void ownerCanChangeRolesAndInvalidContentIsRejected()throws Exception{
  memberships.addMember(group,member);
  mvc.perform(post("/groups/"+group.getGroupId()+"/members/"+member.getUserId()+"/role").with(user("owner")).with(csrf()).session(session(owner)).param("role","MODERATOR")).andExpect(status().is3xxRedirection());assertEquals(Role.MODERATOR,membershipRepo.findByGroupAndUser(group,member).getRole());
  mvc.perform(post("/posts/create").with(user("owner")).with(csrf()).session(session(owner)).param("groupId",group.getGroupId().toString()).param("content"," ")).andExpect(status().isBadRequest());
 }
 @Test void legacyUnscopedApiWritesAreUnavailable()throws Exception{
  mvc.perform(delete("/users/"+owner.getUserId()).with(user("member")).with(csrf())).andExpect(status().isNotFound());
 }
}
