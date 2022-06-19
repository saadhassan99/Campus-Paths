### Design: 3/3
- The ModelAPI should not expose any graph specific implementation details,
since ideally our graph implementation should be able to be swapped out for some
other backend, like a table of shortest paths, or a Google Maps client.
- YOur fields in CampusMap should be private so you don't expose implementation details to the client
### Documentation & Specification (including JavaDoc): 2/3
- You should document type parameters
### Code quality (code and internal comments including RI/AF when appropriate): 2/3
- It's better to use obj instanceof Node<?> instead of .getClass
- CampusMap is an ADT and should be documented as such w/ RI, AF, and checkRep
- your equals method for Path doesn't properly use wildcards
### Testing (test suite quality & implementation): 0/3
- Missing tests
### Mechanics: 3/3

#### Overall Feedback

Great work! Be sure to clean up your uses of generics + your documentation of it, and add tests!

#### More Details

None.
