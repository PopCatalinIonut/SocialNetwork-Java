package main.socialnetwork.utils;

import main.socialnetwork.domain.User;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyAllObs();
}
