% :- [grid].

% agent(0,0,s0).
% teleportal(2,2).
% rock(0,1,s0).
% obstacle(1,0).
% pressurePad(0,2).
% dim(3,3).

% agent(0,0,s0).
% teleportal(2,0).
% rock(1,2,s0).
% obstacle(2,1).
% obstacle(1,0).
% obstacle(1,1).
% pressurePad(2,2).
% dim(3,3).

agent(0,0,s0).
teleportal(0,1).
rock(1,2,s0).
rock(1,0,s0).
obstacle(1,1).
pressurePad(2,2).
pressurePad(2,0).
dim(3,3).


freeSpace(X,Y,S):- \+obstacle(X,Y).

%==================================================UP==========================================================
agent(X,Y,result(A,S)):-
			dim(Xdim,Ydim),
			Xprev is X + 1,
			Xup is X - 1,
			Xup2 is X - 2,

		(
			% freeSpace above agent
	    (A = up, X>=0, Xprev< Xdim, \+obstacle(X,Y), \+rock(X,Y,S), agent(Xprev,Y,S));
			% freeSpace above rock, rock above the agent
			(A = up, Xprev< Xdim, Xup >= 0, rock(X,Y,S), \+obstacle(Xup,Y), \+rock(Xup,Y,S), agent(Xprev,Y,S))

			% presistent state
			% (agent(X,Y,S),A = up, (
			% 												% agent at the border (X = 0) (
			% 												X = 0;
			% 												% obstacle above agent
			% 												( Xup >= 0, obstacle(Xup,Y) );
			% 												% rock above agent and rock at the border (Xup = 0)
			% 												( Xup = 0, rock(Xup,Y,S) );
			% 												% rock above agent and obstacle or another rock above rock
			% 												( Xup2 >= 0 , Xup >= 1, rock(Xup,Y,S), (rock(Xup2,Y,S); obstacle(Xup2,Y)) )
			% 										)
			% 	)
		).

%==================================================LEFT==========================================================
agent(X,Y,result(A,S)):-
		dim(Xdim,Ydim),
		Yprev is Y + 1,
		Yleft is Y - 1,
		Yleft2 is Y - 2,
		(

		% freeSpace to the left of the agent
		(A = left, Y>=0, Yprev< Ydim,\+obstacle(X,Y), \+rock(X,Y,S), agent(X,Yprev,S));
		% freeSpace to the left of a rock, rock to the left of the agent
		(A = left, Yprev < Ydim, Yleft >= 0, rock(X,Y,S), \+obstacle(X,Yleft), \+rock(X,Yleft,S), agent(X,Yprev,S))
		% presistent state
		% (agent(X,Y,S),A = left, (
		% 												% agent at the border (Y = 0) (
		% 												Y = 0;
		% 												% obstacle to the left of the agent
		% 												( Yleft >= 0, obstacle(X,Yleft) );
		% 												% rock to the left of the agent and rock at the border (Yleft = 0)
		% 												( Yleft = 0, rock(X,Yleft,S) );
		% 												% rock to the left of agent and obstacle or another rock to the left of the rock
		% 												( Yleft2 >= 0 , Yleft >= 1, rock(X,Yleft,S), (rock(X,Yleft2,S); obstacle(X,Yleft2)) )
		% 										)
		% 	)
		).

%=============================================Down===========================================
agent(X,Y,result(A,S)):-
		dim(Xdim,Ydim),
		Xprev is X - 1,
		Xdown is X + 1,
		Xdown2 is X + 2,
    (
		% freeSpace below agent.
    (A = down, X<Xdim, Xprev>=0, \+obstacle(X,Y), \+rock(X,Y,S), agent(Xprev,Y,S));
		% freeSpace below rock, rock below the agent
		(A = down, Xprev>=0, Xdown < Xdim,  rock(X,Y,S), \+obstacle(Xdown,Y), \+rock(Xdown,Y,S), agent(Xprev,Y,S))
		% presistent state
		% (agent(X,Y,S),A = down, ( Xdim1 is Xdim - 1,
		%
		% 												(
		% 												  % agent at the border (X = Xdim1)
		% 													X = Xdim1;
		% 													% obstacle below the agent
		% 													( Xdown < Xdim, obstacle(Xdown,Y) );
		% 													% rock beow the agent and rock at the border (Xdown = Xdim1)
		% 													( Xdown = Xdim1, rock(Xdown,Y,S) );
		% 													% rock below the agent and obstacle or another rock below the rock
		% 													( Xdown2 < Xdim , Xdown < Xdim1, rock(Xdown,Y,S), (rock(Xdown2,Y,S); obstacle(Xdown2,Y)) )
		% 												)
		% 										)
		% 	)
		).

%=============================================Right=============================================
agent(X,Y,result(A,S)):-
		dim(Xdim,Ydim),
		Yprev is Y - 1,
		Yright is Y + 1,
		Yright2 is Y + 2,
		(
		% freeSpace to the right of the agent
		(A = right, Y < Ydim, Yprev >= 0, \+obstacle(X,Y), \+rock(X,Y,S), agent(X,Yprev,S));
		% freeSpace to the right of a rock, rock to the right of the agent
		(A = right, Yprev >= 0, Yright < Ydim, rock(X,Y,S), \+obstacle(X,Yright), \+rock(X,Yright,S), agent(X,Yprev,S))
		% presistent state
		% (agent(X,Y,S),A = right, ( Ydim1 is Ydim - 1,
		%
		% 												(
		% 												  % agent at the border (Y = Ydim1)
		% 													Y = Ydim1;
		% 													% obstacle to the right of the agent
		% 													( Yright < Ydim, obstacle(X,Yright) );
		% 													% rock to the right of the agent and rock at the border (Yright = Ydim1)
		% 													( Yright = Ydim1, rock(X,Yright,S) );
		% 													% rock to the right of the agent and obstacle or another rock to the right of the rock
		% 													( Yright2 < Ydim , Yright < Ydim1, rock(X,Yright,S), (rock(X,Yright2,S); obstacle(X,Yright2)) )
		% 												)
		% 										)
		% 	)
		).

%=============================================UP=============================================
rock(X,Y,result(A,S)):-
	dim(Xdim,Ydim),
	Xprev is X + 1,
	Xdown is X + 2,
  Xup is X - 1,
	(
	  %freeSpace above rock.
		(A = up, X>=0, Xprev< Xdim, Xdown < Xdim, \+obstacle(X,Y), agent(Xdown,Y,S), \+rock(X,Y,S), rock(Xprev,Y,S));
	  %persistent state
		(rock(X,Y,S),( \+A = up; (
														(
														  % rock is at the border (X = 0)
															X = 0;
															% obstacle or rock above the rock
															( Xup >= 0, (obstacle(Xup,Y); rock(Xup,Y,S); (Xdown < Xdim, \+agent(Xdown,Y,S)) ) )
														)
												))
			)
  ).

%=============================================LEFT=============================================
rock(X,Y,result(A,S)):-
	dim(Xdim,Ydim),
	Yprev is Y + 1,
	Yright is Y + 2,
	Yleft is Y - 1,
	(
	%freeSpace to the left of a rock.
	(A = left, Y >= 0, Yprev< Ydim, Yright < Ydim, \+obstacle(X,Y), agent(X,Yright,S), \+rock(X,Y,S), rock(X,Yprev,S));
	%persistent state
	(rock(X,Y,S), (\+A = left; (
													(
														% rock is at the border (Y = 0)
														Y = 0;
														% obstacle or rock to the left of the rock
														( Yleft >= 0, (obstacle(X,Yleft); rock(X,Yleft,S); (Yright < Ydim, \+agent(X,Yright,S))) )
													)
											))
		)
	).

%=============================================DOWN=============================================
rock(X,Y,result(A,S)):-
	dim(Xdim,Ydim),
	 Xprev is X - 1,
	 Xup is X - 2,
   Xdown is X + 1,
	 (
   %freeSpace below rock.
	(A = down, X<Xdim, Xprev>=1, Xup>=0, \+obstacle(X,Y), agent(Xup,Y,S), \+rock(X,Y,S), rock(Xprev,Y,S));
	%persistent state
	(rock(X,Y,S), (\+A = down; ( Xdim1 is Xdim - 1,
													(
														% rock is at the border (X = Xdim1)
														X = Xdim1;
														% obstacle or rock belowthe rock
														( Xdown < Xdim, (obstacle(Xdown,Y); rock(Xdown,Y,S); (Xup>=0, \+agent(Xup,Y,S))) )
													)
											))
		)
  ).

%=============================================RIGHT=============================================
rock(X,Y,result(A,S)):-
		dim(Xdim,Ydim),
		 Yprev is Y - 1,
		 Yleft is Y - 2,
	   Yright is Y + 1,
		 (
	   %freeSpace to the right rock.
		(A = right, Y < Ydim, Yprev >= 1, Yleft >= 0, \+obstacle(X,Y), agent(X,Yleft,S), \+rock(X,Y,S), rock(X,Yprev,S));
		%persistent state
		(rock(X,Y,S), (\+A = right; ( Ydim1 is Ydim - 1,
														(
															% rock is at the border (Y = Ydim1)
															Y = Ydim1;
															% obstacle or rock to the right of rock
															( Yright < Ydim, (obstacle(X,Yright); rock(X,Yright,S); (Yleft >=0, \+agent(X,Yleft,S))) )
														)
												))
			)
	  ).


solve(Q,C,R):-
    call_with_depth_limit(Q,C,R),
    R \= depth_limit_exceeded.
solve(Q,C,R):-
    call_with_depth_limit(Q,C,R),
    R = depth_limit_exceeded,
    C1 is C + 1,
    solve(Q,C1,R2).
