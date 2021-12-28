package agh.ics.oop;

public interface IPositionChangeObservable {

        void addObserver(IPositionChangeObserver observer);
        void removeObserver(IPositionChangeObserver observer);
        void positionChange();

}
