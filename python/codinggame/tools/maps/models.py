class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __str__(self):
        return f'{self.x} {self.y}'

    def __eq__(self, other):
        return isinstance(other, Point) and other.x == self.x and other.y == self.y

class Map:

    def __init__(self):
        self.points = []

    def add(self,point:Point)->"Map":
        if not point in self.points:
            self.points.append(point)
        return self

    def __str__(self):
        return f"[{','.join( str(p) for p in self.points)}]"

p1 = Point(10,20)
p2 = Point(10,20)

map = Map().add(p1).add(p2)
print(f" map {map}")
print(f" are equals {p1 == p2}")
