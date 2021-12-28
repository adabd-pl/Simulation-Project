package agh.ics.oop;

public interface IChildrenObservable {
    void addObserver(IChildrenObserver observer);
    void removeObserver(IChildrenObserver observer);
    void newChildren();
}
