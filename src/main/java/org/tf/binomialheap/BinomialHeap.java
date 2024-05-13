package org.tf.binomialheap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BinomialHeap<T> {
    @Setter
    @Getter
    private static int operations; // Для тестирования
    private Node<T> head;

    public BinomialHeap() {
    }

    private BinomialHeap(Node<T> head) {
        this.head = head;
    }

    public Node<T> insert(long key, T value) {
        Node<T> node = new Node<>(key, value);
        BinomialHeap<T> binomialHeap = new BinomialHeap<>(node);
        ++operations;
        head = adjust(mergeBiHeaps(this, binomialHeap)).getHead();
        return node;
    }

    public Node<T> deleteMin() {
        Node<T> minNode = min();
        Node<T> prevNode = null;
        Node<T> currentNode = head;
        while (currentNode != null) {
            ++operations;
            if (currentNode.equals(minNode)) {
                if (prevNode != null) {
                    if (currentNode.getSibling() == null) prevNode.setSibling(null);
                    else prevNode.setSibling(currentNode.getSibling());
                }
                else { // Значит minNode == head
                    if (currentNode.getSibling() != null) head = head.getSibling();
                }
                break;
            }
            prevNode = currentNode;
            currentNode = currentNode.getSibling();
        }

        currentNode = minNode.getChild();
        Node<T> prevPrevNode;
        prevNode = null;
        while (currentNode != null) {
            prevPrevNode = prevNode;
            prevNode = currentNode;
            currentNode = currentNode.getSibling();
            prevNode.setSibling(prevPrevNode);
            ++operations;
        }

        if (minNode.getChild() != null) {
            minNode.getChild().setSibling(null);
            ++operations;
        }

        head = adjust(mergeBiHeaps(this, new BinomialHeap<>(prevNode))).getHead();
        return minNode;
    }

    public Node<T> min() {
        Node<T> currentNode = head;
        Node<T> result = currentNode;
        while (currentNode != null && currentNode.getSibling() != null) {
            ++operations;
            if (currentNode.compareTo(currentNode.getSibling()) > 0) result = currentNode.getSibling();
            currentNode = currentNode.getSibling();
        }
        return result;
    }

    public void decreaseKey(Node<T> node, long newKey) {
        if (node.getKey() > newKey) {
            node.setKey(newKey);
            Node<T> currentNode = node;
            Node<T> parent;
            while ((parent = currentNode.getParent()) != null
                    && currentNode.getKey() < parent.getKey()) {
                long tempKey = currentNode.getKey();
                T tempValue = currentNode.getValue();
                currentNode.setKey(parent.getKey());
                currentNode.setValue(parent.getValue());
                parent.setKey(tempKey);
                parent.setValue(tempValue);
                ++operations;
                currentNode = parent;
            }
        }
    }

    public Node<T> delete(Node<T> node) {
        decreaseKey(node, Long.MIN_VALUE);
        return deleteMin();
    }

    private static <T> void mergeTree(Node<T> child, Node<T> parent) {
        child.setParent(parent);
        child.setSibling(parent.getChild());
        parent.setChild(child);
        parent.setDegree(parent.getDegree() + 1);
        ++operations;
    }

    public static <T> BinomialHeap<T> mergeBiHeaps(BinomialHeap<T> heap1, BinomialHeap<T> heap2) {
        if (heap1 == null || heap1.getHead() == null) return heap2;
        else if (heap2 == null || heap2.getHead() == null) return heap1;

        Node<T> resultHead;
        Node<T> head1 = heap1.getHead();
        Node<T> head2 = heap2.getHead();
        // Фаза 1:
        if (head1.getDegree() < head2.getDegree()) {
            resultHead = head1;
            head1 = head1.getSibling();
        }
        else {
            resultHead = head2;
            head2 = head2.getSibling();
        }
        ++operations;

        Node<T> currentRoot = resultHead;
        while (head1 != null && head2 != null) {
            if (head1.getDegree() < head2.getDegree()) {
                currentRoot.setSibling(head1);
                head1 = head1.getSibling();
            }
            else {
                currentRoot.setSibling(head2);
                head2 = head2.getSibling();
            }
            ++operations;
            currentRoot = currentRoot.getSibling();
        }
        while (head1 != null) {
            currentRoot.setSibling(head1);
            head1 = head1.getSibling();
            currentRoot = currentRoot.getSibling();
            ++operations;
        }
        while (head2 != null) {
            currentRoot.setSibling(head2);
            head2 = head2.getSibling();
            currentRoot = currentRoot.getSibling();
            ++operations;
        }
        return new BinomialHeap<>(resultHead);
    }

    public static <T> BinomialHeap<T> adjust(BinomialHeap<T> heap) {
        Node<T> currentRoot = heap.getHead();
        Node<T> prevNode = null;
        while (currentRoot != null && currentRoot.getSibling() != null) {
            Node<T> sibling = currentRoot.getSibling();
            if (currentRoot.getDegree() != sibling.getDegree()) {
                prevNode = currentRoot;
                currentRoot = currentRoot.getSibling();
            }
            else {
                if (sibling.getSibling() != null
                        && sibling.getDegree() == sibling.getSibling().getDegree()) {
                    prevNode = currentRoot;
                    currentRoot = currentRoot.getSibling();
                }
                else if (currentRoot.getKey() < sibling.getKey()) { // У кого меньше корень, тот родитель
                    currentRoot.setSibling(sibling.getSibling() != null ? sibling.getSibling() : null);
                    mergeTree(sibling, currentRoot);
                }
                else {
                    if (prevNode != null) prevNode.setSibling(sibling);
                    else heap.setHead(sibling);
                    mergeTree(currentRoot, sibling);
                }
            }
            ++operations;
        }
        return new BinomialHeap<>(heap.getHead());
    }

    public void printHeads() {
        System.out.println("START PRINT");
        Node<T> currentNode = head;
        while (currentNode != null) {
            System.out.println("k" + currentNode.getKey());
            if (currentNode.getChild() != null) System.out.println("c" + currentNode.getChild().getKey());
            currentNode = currentNode.getSibling();
        }
        System.out.println("END PRINT");
    }
}

