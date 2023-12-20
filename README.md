# Solar-Lander
a small java orbit-sim project using the [libGDX](https://libgdx.com/) framework with a controllable lander 

![](assets/preview.png)
![](assets/preview2(new).png)
![](assets/preview3(fixed).gif)
# Keybinds
```WASD``` : Movement \
```V (HOLD)``` : Show Force Vectors of Lander(redundant, see bottom boxes) \
```Space (HOLD)``` : Show Hitboxes \
```Z (HOLD)``` : -10x Zoom Out \
```X (HOLD)``` : -100x Zoom Out
# How it Works
## Physics
The program uses primarily uses two physics equations to determine orbits and velocities

(1). The Law of Gravitation: F = G * (m1 * m2) / r^2
- F is the gravitational force between the two objects/planets
- G is the universal gravitational constant, 6.674 x 10^-11
- m1 and m2 are the masses of the two objects
- r is the distance between the center of mass for each object.\

(2). Orbital Velocity: V = SQRT((G * M) / R)
- V is the orbital velocity
- G is the universal gravitational constant, 6.674 x 10^-11
- M is the mass of the central body(assuming orbiting body has insignificant mass in comparison)
- R is the distance between the center of mass for each object.

## Code
### Generation
- the program starts by generating an array of solarObjects ```solarSystem[]```
- ```solarSystem[0]``` is reserved for the central body / sun
- other planets are generated with random radii [700 - 200] and corresponding mass based on (4/3) * pi * (radius)
- then it gets assigned a random position between [10,000 , 0] and [10,000 , 10,000]
- if the planet fails ```orbitCheck()``` (it overlaps another planet) it gets new properties
- planets are assigned a starting velocity based on equation (2)
  - by default the starting velocity puts them into a perfect circular orbit

### Game-Loop
- position and velocity changes are calculated for both ```lander``` and ```solarSystem[]```
- the main viewport ```extendView``` is updated and the lander sprite are rendered with a ```SpriteBatch```
- planets and other objects are rendered using a ```ShapeRender``` object on ```extendView```
- the ui/labels are then updated and rendered on viewport ```screenView```
- the minimap viewport ```miniView``` is updated and has the scaled down system rendered within