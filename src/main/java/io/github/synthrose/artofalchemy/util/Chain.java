package io.github.synthrose.artofalchemy.util;

import java.util.*;

public class Chain {

    public static <K, V> ChainMap<K, V> start(Map<K, V> map) {
        return new ChainMap<K, V>(map);
    }

    public static <E> ChainList<E> start(List<E> list) {
        return new ChainList<E>(list);
    }

    public static <E> ChainSet<E> start(Set<E> set) {
        return new ChainSet<E>(set);
    }

    public static class ChainMap<K, V> {
        private Map<K, V> contents;

        private ChainMap(Map<K, V> map) {
            contents = map;
        }

        public ChainMap<K, V> put(K key, V value) {
            contents.put(key, value);
            return ChainMap.this;
        }

        public ChainMap<K, V> putIfAbsent(K key, V value) {
            contents.putIfAbsent(key, value);
            return ChainMap.this;
        }

        public ChainMap<K, V> putAll(Map<K, V> map) {
            contents.putAll(map);
            return ChainMap.this;
        }

        public Map<K, V> finish() {
            return contents;
        }
    }

    public static class ChainList<E> {
        private List<E> contents;

        private ChainList(List<E> list) {
            contents = list;
        }

        public ChainList<E> add(E element) {
            contents.add(element);
            return ChainList.this;
        }

        public ChainList<E> add(int i, E element) {
            contents.add(i, element);
            return ChainList.this;
        }

        public ChainList<E> addAll(List<E> list) {
            contents.addAll(list);
            return ChainList.this;
        }

        public ChainList<E> addAll(int i, List<E> list) {
            contents.addAll(i, list);
            return ChainList.this;
        }

        public List<E> finish() {
            return contents;
        }
    }

    public static class ChainSet<E> {
        private Set<E> contents;

        private ChainSet(Set<E> set) {
            contents = set;
        }

        public ChainSet<E> add(E element) {
            contents.add(element);
            return ChainSet.this;
        }

        public ChainSet<E> addAll(Set<E> set) {
            contents.addAll(set);
            return ChainSet.this;
        }

        public Set<E> finish() {
            return contents;
        }
    }
}
