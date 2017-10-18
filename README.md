## Splendid

Splendid is a game inspired by [Splendor](http://www.spacecowboys.fr/splendor), a board game published originally by
Space Cowboys.

The objective of the game is to gather points by obtaining _resource factories_. The first player to obtain the required
number of points triggers the last round to start, and the player with most points by the end of the last round becomes
the winner.

### How to play

During gameplay players take turns to perform one of two actions:
 * obtain _resources_ from the resource stacks, or
 * obtain _resource factories_ from the factory board.

Once a player performs an action, the turn is passed to the next player, in a clockwise-fashion.

#### Player summary

Each player's status is represented by a box containing five colored fields, with player's name and score written
underneath. These five fields represent five different resource types: fire (red), earth (green), water (blue), air
(orange), and aether (grey). Each of the fields in a player's box consists of the resource symbol, number of _resource
factories_ of the given type (inset in a square), and number of resources of the given (inset in a circle) that the
player owns.

The player controlled by the user has it's status box located in the bottom part of the browser, centered in the view.
Other players' status boxes are located on the sides of the view. The player currently taking turn has their box
surrounded by two triangular indicators.

#### Resource stacks

At the beginning of the game, the resources are all located on the stacks in the middle part of the view. For each of
the resource types there are five resource tokens available. A player can obtain the resources from the stack by
clicking on the resource tokens and clicking the _Take_ button on the right side of the resource stacks. To activate
the button, a player needs to select either:
 * two resources of a single type, or
 * three resources, each of a different type.
Taking selected resources removes them from the stacks and increases the acting player's resource count.

#### Factory board

Resource factories are represented with a rectangular fields, organized in a grid located in the upper middle part of
the view. Each resource factory is assigned a resource type, represented by that resource's color and symbol. Collecting
a factory requires paying its cost, which is listed on the right-hand side of the resource's field. For each
of the resource types in a factory's cost, a field containing that cost will light up once the user's player collects
enough resources of that type. Once a factory's full cost is met, a player can select that factory by clicking on it,
and then use the _Take_ button next to the resource stacks to collect that factory. Collecting a factory grants a
single, permanent resource of that factory's type, as well as some number of points indicated on the factory's field.
The collected factory is removed from the board, and a new factory is put in its place.

Paying for a factory always makes use of the permanent resources from the previously obtained factories. If amount of
permanent resources is not enough to pay for a factory, resources obtained from the resource stacks are used. These
stack resources are, upon collection of a factory, deducted from the acting player's count, and returned to the stacks.


