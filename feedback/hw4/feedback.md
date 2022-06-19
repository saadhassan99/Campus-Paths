### Code Quality Score: 2/3

- Missing checkRep() calls in RatPoly/RatPolyStack for certain methods, checkRep does not assume that methods were implemented properly, regardless of
how trivial or innocuous they seem.  So, in general, even observer methods need calls to checkRep at their beginning and end.
- Remember to clean up your code and remove all TODO comments prior to submission, and unused exceptions.
- Do not forget to write loop invariants for all loops used in your code!

### Mechanics: 3/3

#### Overall Feedback

- Good work, just remember to call checkRep() in appropriate places.

#### More Details
