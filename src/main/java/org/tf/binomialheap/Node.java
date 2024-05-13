package org.tf.binomialheap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node<T> implements Comparable<Node<T>> {
    private long key; // Приоритет узла
    private T value; // Значение
    private long degree; // Степень узла(кол-во дочерних узлов)
    private Node<T> parent; // Указатель на родительский узел
    private Node<T> child; // Указатель на крайний левый дочерний узел
    private Node<T> sibling; // Указатель на правый сестринский узел

    protected Node(long key, T value) {
        this.key = key;
        this.value = value;
    }

    protected Node(long key, T value, Node<T> parent, Node<T> child, Node<T> sibling) {
        this.key = key;
        this.value = value;
        this.parent = parent;
        this.child = child;
        this.sibling = sibling;
        degree = 2;
    }

    @Override
    public int compareTo(Node<T> node) {
        return Long.compare(this.key, node.key);
    }
}
