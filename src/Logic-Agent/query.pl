:- [main].

search() :-
solve(
 (				rock(0,2,S),
					agent(1,1,S)
 ),1,R).
