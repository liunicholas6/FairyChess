=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Complex Game Logic
    It's chess, where you can't put yourself into check, and en passant and castle
    exist. Not much more than that.

  2. 2D Array
    The board is represented as a 2D array. A 2D array is just the clearest way
    to represent a grid in the OO world.

  3. Map
    MoveHolder is implemented as a map such that if one were to make Fairy Chess pieces,
    only one move could exist per target location. Additionally, a map is used from filePath
    to BufferedImage in ChessDisplay so that each piece image gets loaded once.

  4. Inheritance and subtyping
    The MoveGenerator interface is used by a whole lot of different move generators, and the
    move class is extended for the purposes of promotion and castling.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Note that this was originally supposed to be Fairy Chess, so much of the code is
  more modular than appropriate for standard Chess.

  RunChess - the top level runnable
  ChessDisplay - the JPanel that loads in the image files and displays the game
  Chess - The high-level game logic that links to the display
  GameState - A class containing the type of state of the game and the player to
                whom it corresponds (e.g. checkmate state for Player 1)
  PieceFactory - Factory class for making pieces
  Piece - Represents a piece
  IMoveGenerator - interface for a generator class that generates all the moves of
            a game starting from an input position given certain rules
            - Leaper - moves to any square that has a set distance away
                A knight is a (1, 2) leaper
            - Rider - like a leaper, but can repeat as many times as necessary.
                A rook is a (0, 1) rider and a bishop is a (1, 1) rider.
            - Compound - a combination of multiple move generators. A queen is a rook
                bishop compound;
            - Crown - moves any direction one step. A king is a crown with some extra rules
            - Promoter - alters moves that fit a condition to promote a piece as well as moving.
                A pawn is a Promoter of a PawnMoveGenerator
            - PawnMoveGenerator - handles pawns.
  Move - Represents a move on the board
            - PromotionMove and CastleMove both are variations on Move.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    Many. I didn't plan everything out as well as I should have, and refactored much along the way.
    For some reason I decided it was a good idea to ditch the Tile class, which I still regret (see
    below).

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    There's a lot of things that I would like to have done better. There are three things
    that I would really like to have fixed if I had more time.

     - Move absolutely should be an interface instead of a class. As things are currently
       implemented, most moves carry around meaningless information. Moves that don't capture
       anything have a capturePosition, promotionMoves carry the same instance variable twice
       (once as their own and a second time as the move they wrap), and castling moves are hot
       garbage.

     - Board should be a 2D array of records holding a position and a Piece (Tile class),
       and perhaps some other contextual data for other versions of fairy chess, and Piece
       should be an immutable class. As is, to check future board states, I have to clone
       pieces which is really inefficient. Furthermore, checking for check requires a second
       duplication of kings in the current implementation in a way that took a long time to figure
       out, while the same reference could be referred to with a Tile class.

     - The current implementation of castling is complete spaghetti, largely as a result of
       the previous two things mentioned. It is the only move not implemented modularly, which
       makes me sad.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  I got my chess sprites from here https://opengameart.org/content/chess-pieces-and-board-squares

  I also referenced wikipedia for fairy chess pieces and Chess.com for general chess rules.