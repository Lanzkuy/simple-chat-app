package com.lacorp.simple_chat_app.utils;

import com.google.android.gms.tasks.Task;
import com.lacorp.simple_chat_app.data.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Resource<T> {
    @Nonnull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final Throwable throwable;

    public Resource(@Nonnull Status status, @Nullable T data, @Nullable Throwable throwable) {
        this.status = status;
        this.data = data;
        this.throwable = throwable;
    }

    public static <T> Resource<T> Success(@Nonnull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> Failure(@Nonnull Throwable throwable) {
        return new Resource<>(Status.FAILURE, null, throwable);
    }

    public static <T> Resource<T> Loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, FAILURE, LOADING };
}
