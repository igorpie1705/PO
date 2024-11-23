package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap<Animal, Vector2d> {

    protected final Map<Vector2d, Animal> animals = new HashMap<>();

    protected int rightWidth = 0;
    protected int rightHeight = 0;
    protected int leftWidth = 0;
    protected int leftHeight = 0;

    @Override
    public void move(Animal animal, MoveDirection direction) {
        animals.remove(animal.getPosition(), (Animal) animal);
        animal.move(direction, this);
        animals.put(animal.getPosition(), animal);
    }

    @Override
    public String toString() {
        MapVisualizer toDraw = new MapVisualizer(this);
        return toDraw.draw(getCurrentBounds().lowerLeft(), getCurrentBounds().upperRight());
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if(canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(), animal);
        }
        else{
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(new Vector2d(rightWidth, rightHeight))) &&
                (position.follows(new Vector2d(leftWidth, leftHeight))) &&
                !isOccupied(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    @Override
    public Boundary getCurrentBounds(){
        Vector2d left = new Vector2d(leftWidth, leftHeight);
        Vector2d right = new Vector2d(rightWidth, rightHeight);
        List<WorldElement> elements = getElements();
        for(WorldElement element : elements){
            left = left.lowerLeft(element.getPosition());
            right = right.upperRight(element.getPosition());
        }
        return new Boundary(left, right);
    }
}
