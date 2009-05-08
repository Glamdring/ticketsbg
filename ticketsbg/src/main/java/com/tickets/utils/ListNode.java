package com.tickets.utils;

public class ListNode<E> {
    private E value;
    private ListNode<E> next;

    public E getValue() {
        return value;
    }
    public void setValue(E value) {
        this.value = value;
    }
    public ListNode<E> getNext() {
        return next;
    }
    public void setNext(ListNode<E> next) {
        this.next = next;
    }
}
