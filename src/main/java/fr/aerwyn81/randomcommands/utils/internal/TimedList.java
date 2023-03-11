package fr.aerwyn81.randomcommands.utils.internal;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class TimedList<T> implements Iterable<T> {
    private final ArrayList<TimedItem<T>> list = new ArrayList<>();

    public TimedList() {
        Timer timer = new Timer(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                list.removeIf(TimedItem::isExpired);
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void add(T item, long timeoutMillis) {
        list.add(new TimedItem<>(item, System.currentTimeMillis() + timeoutMillis));
    }

    public void remove(T item) {
        get(item).ifPresent(list::remove);
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Optional<TimedItem<T>> get(T item) {
        return list.stream().filter(tTimedItem -> tTimedItem.item == item).findFirst();
    }

    // Return true if at least one item in the otherList to be compared exists
    public boolean disjoint(ArrayList<T> otherList) {
        var c = list.stream()
                .map(TimedItem::item);

        var d = new HashSet<>(otherList);

        var x = c.anyMatch(d::contains);

        return x;
    }

    @Override
    public Iterator<T> iterator() {
        return new TimedListIterator<>(list.iterator());
    }

    private record TimedItem<T>(T item, long expireTime) {

        public boolean isExpired() {
                return System.currentTimeMillis() > expireTime;
            }
        }

    private record TimedListIterator<T>(Iterator<TimedItem<T>> iterator) implements Iterator<T> {

        @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next().item();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }
}