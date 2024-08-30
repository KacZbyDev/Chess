package com.example.chess;

@FunctionalInterface
public interface TriFunction<U, V, R> {
    R apply(U u, V v);
}
