package com.jonathan.bankapp.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemsHome {
    private int viewType;
    @Nullable
    private String text;
    private int icon;
    @Nullable
    private String text_one;
    @Nullable
    private String text_two;
    public static final int ItemWithdrawals = 0;
    public static final int ItemPractice = 1;
    public static final int ItemHut = 2;
    public static final int ItemEmail = 3;
    public static final int ItemPassword = 4;
    @NotNull

    public final int getViewType() {
        return this.viewType;
    }

    public final void setViewType(int var1) {
        this.viewType = var1;
    }

    @Nullable
    public final String getText() {
        return this.text;
    }

    public final void setText(@Nullable String var1) {
        this.text = var1;
    }

    @Nullable
    public final String getText_one() {
        return this.text_one;
    }

    public final void setText_one(@Nullable String var1) {
        this.text_one = var1;
    }

    @Nullable
    public final String getText_two() {
        return this.text_two;
    }

    public final void setText_two(@Nullable String var1) {
        this.text_two = var1;
    }

    public final int geticon() {
        return this.icon;
    }

    public final void seticon(int icon) {
        this.icon = icon;
    }

    public ItemsHome(int viewType, @Nullable String text) {
        this.text = text;
        this.viewType = viewType;
    }

    public ItemsHome(int viewType, int icon, @Nullable String text_one, @Nullable String text_two) {
        this.icon = icon;
        this.text_one = text_one;
        this.text_two = text_two;
        this.viewType = viewType;
    }

    public static final class Companion {
        private Companion() {
        }

    }
}

