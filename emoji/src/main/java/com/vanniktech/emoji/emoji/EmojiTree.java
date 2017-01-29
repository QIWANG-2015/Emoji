package com.vanniktech.emoji.emoji;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SparseArrayCompat;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class EmojiTree {
    private final EmojiNode root = new EmojiNode(null);

    public void add(@NonNull final Emoji emoji) {
        final String unicode = emoji.getUnicode();

        EmojiNode current = root;

        for (int i = 0; i < unicode.length() - 1; i++) {
            current = current.appendOrGet(unicode.charAt(i));
        }

        current.appendLast(unicode.charAt(unicode.length() - 1), emoji);
    }

    @Nullable
    public Emoji findEmoji(@NonNull final CharSequence candidate) {
        EmojiNode current = root;
        Emoji result = null;
        int i = 0;

        for (; i < candidate.length(); i++) {
            current = current.getChild(candidate.charAt(i));

            if (current == null) {
                break;
            } else if (current.getEmoji() != null) {
                result = current.getEmoji();
            }
        }

        return result;
    }

    public boolean isEmpty() {
        return root.children.size() <= 0;
    }

    private static class EmojiNode {
        private final SparseArrayCompat<EmojiNode> children = new SparseArrayCompat<>();
        private Emoji emoji;

        EmojiNode(@Nullable final Emoji emoji) {
            this.emoji = emoji;
        }

        @Nullable
        EmojiNode getChild(final char child) {
            return children.get(child);
        }

        @Nullable
        Emoji getEmoji() {
            return emoji;
        }

        void setEmoji(@NonNull final Emoji emoji) {
            this.emoji = emoji;
        }

        @NonNull
        EmojiNode appendOrGet(final char child) {
            EmojiNode existing = children.get(child);

            if (existing == null) {
                existing = new EmojiNode(null);

                children.put(child, existing);
            }

            return existing;
        }

        void appendLast(final char child, @NonNull final Emoji newEmoji) {
            EmojiNode existing = children.get(child);

            if (existing == null) {
                existing = new EmojiNode(newEmoji);

                children.put(child, existing);
            } else {
                existing.setEmoji(newEmoji);
            }
        }
    }
}