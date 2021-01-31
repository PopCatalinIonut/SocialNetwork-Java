package main.socialnetwork.domain;

import main.socialnetwork.utils.Constants;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long, Long>> {
    LocalDateTime date;

    public Friendship(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return  "Utilizatorul " + getId().getLeft() + " cu utilizatorul " + getId().getRight() + " in data: " + date.format(Constants.DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Friendship) {
            Friendship that = (Friendship) obj;
            return getId().getLeft().equals(that.getId().getLeft()) && getId().getRight().equals(that.getId().getRight()) ||
                    getId().getLeft().equals(that.getId().getRight()) && getId().getRight().equals(that.getId().getLeft());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int)((getId().getRight() ^ getId().getLeft()) % Integer.MAX_VALUE);
    }
}
