# GatherLink web modules

The current application uses authenticated Spring MVC pages; the earlier unrestricted entity CRUD controllers are retired.

- `controller/`: login/registration, group discovery/creation, membership, posts and owner role actions.
- `security/`: BCrypt authentication, session access and CSRF protection.
- `service/`: account validation, group authorization, transactional membership and posting.
- `model/` and `repository/`: JPA persistence and uniqueness constraints.

Joining is implemented. Leaving and comment workflows are not advertised as complete. See [the root README](../../../../../README.md) for verified commands and current limitations, and [source layout](../../../FolderStructure_README.md).
