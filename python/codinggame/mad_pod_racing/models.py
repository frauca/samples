import math
class Point:
    def __init__(self, x: int, y: int):
        self.x:int = x
        self.y:int = y

    @staticmethod
    def none():
        return Point(-1,-1)
    def __str__(self):
        return f'{self.x} {self.y}'

    def __eq__(self, other):
        return isinstance(other, Point) and other.x == self.x and other.y == self.y

class Trigonometry:
    pass
    @staticmethod
    def vector(p1: Point, p2: Point) -> Point:
        return Point(p1.x - p2.x, p1.y - p2.y)

    @staticmethod
    def distance(p1: Point, p2: Point)-> float:
        return math.sqrt((p2.x -p1.x) ** 2 + (p2.y - p1.y) ** 2)

    @staticmethod
    def calculate_angle(p1: Point, p2: Point, p3: Point) -> float:
        # Calculate the vectors AB and BC
        vector_ab = Trigonometry.vector(p1, p2)
        vector_bc = Trigonometry.vector(p2, p3)

        # Calculate the dot product of AB and BC
        dot_product = vector_ab.x * vector_bc.x + vector_ab.y * vector_bc.y

        # Calculate the magnitudes of AB and BC
        magnitude_ab = math.sqrt(vector_ab.x ** 2 + vector_ab.y ** 2)
        magnitude_bc = math.sqrt(vector_bc.x ** 2 + vector_bc.y ** 2)

        # Calculate the cosine of the angle
        cosine_angle = dot_product / (magnitude_ab * magnitude_bc)

        # Calculate the angle in radians
        angle_radians = math.acos(cosine_angle)

        # Convert the angle to degrees
        angle_degrees = math.degrees(angle_radians)

        return angle_degrees

class Map:
    def __init__(self):
        self.last_point:Point = Point.none()
        self.points:list[Point] = []
        self.lap:int = 0

    def add(self, point: Point) -> "Map":
        if not self.last_point == point:
            self.last_point = point
            if not point in self.points:
                self.points.append(point)
            else:
                if self.lap == 0 or self.points[-1] == point:
                    self.lap += 1
        return self

    def next_point(self, point: Point) -> Point:
        if self.lap == 0:
            return Point.none()
        num_of_points = len(self.points)
        for i in range(num_of_points):
            p = self.points[i]
            if p == point:
                return self.points((i+1)%num_of_points)
        return Point.none()
    def __str__(self):
        return f"[{','.join(str(p) for p in self.points)}]"

class Ship:

    def __init__(self):
        self.pos:Point = Point(0,0)
        self.last_pos:Point = self.pos

    def new_pos(self, pos:Point)->"Ship":
        self.last_pos = self.pos
        self.pos = pos
        return self

    def velocity(self)->float:
        return Trigonometry.distance(self.pos, self.last_pos)



# Example usage

angle = Trigonometry.calculate_angle(Point(0, 0), Point(1, 0),Point( 1, 1))
print(f"The angle is: {angle} degrees")
