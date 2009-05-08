package com.tickets.utils;

import java.util.List;

public class CircularLinkedList<E> {

    private ListNode<E> head;

    public CircularLinkedList() {

    }

    public CircularLinkedList(List<E> list) {
        for (E e : list) {
            add(e);
        }
    }

    public void add(E e){
        ListNode<E> node = new ListNode<E>();
        node.setValue(e);

        if (head == null)
            head = node;

        node.setNext(head);
        getLast().setNext(node);
    }

    public ListNode<E> getLast() {
        ListNode<E> node = head;
        if (head == null)
            return null;

        do {
            node = node.getNext();
        } while (node.getNext() != head);

        return node;
    }

    public void remove(int position) {
        ListNode<E> node = head;
        for (int i = 0; i < position; i ++) {
            if (i == position - 1) {
                node.setNext(node.getNext().getNext());
            } else {
                node = node.getNext();
            }
        }

        if (position == 0)
            head = head.getNext();
    }

    /**
     * @param nodeToRemove
     * @return the node after the deleted one
     */
    public ListNode<E> remove(ListNode<E> nodeToRemove) {
        if (head == null)
            return null;

        ListNode<E> node = getLast();

        int i = 0;
        int size = size();
        while (i < size) {
            if (node.getNext() == nodeToRemove) {
                node.setNext(node.getNext().getNext());

                if (nodeToRemove == head)
                    head = node.getNext();

                break;
            }
            i++;
            node = node.getNext();
        }
        return node.getNext();
    }

    public void display() {
        ListNode<E> node = head;
        do {
            System.out.println(node.getValue() + ", ");
            node = node.getNext();
        } while (node != head);
    }

    /**
     * @return the number of nodes (elements) in this list
     */
    public int size() {
        if (head == null)
            return 0;

        int i = 0;
        ListNode<E> node = head;
        do {
            i++;
            node = node.getNext();
        } while (node != head);

        return i;
    }

    public ListNode<E> get(int position) {
        if (position >= size())
            throw new ArrayIndexOutOfBoundsException(position);

        ListNode<E> node = head;
        for (int i = 0; i < position; i ++) {
            node = node.getNext();
        }

        return node;
    }
}
