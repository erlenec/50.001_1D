package com.example.a500011d;

import androidx.annotation.Nullable;

public class ItemEntry {
    private String username;
    private Status status;
    private String date_created;
    private String date_updated;
    private String item;
    private String location;
    private String description;
    @Nullable private String imagePath;

    enum Status {
        FOUND,
        LOST,
        RESOLVED,
        EXPIRED;

        @Override
        public String toString() {
            switch(this) {
                case FOUND:
                    return "Found";

                case LOST:
                    return "Lost";

                case RESOLVED:
                    return "Item has been found/returned";

                case EXPIRED:
                    return "Expired";
            };
            throw new IllegalArgumentException();
        }
    }

    public ItemEntry() {
        // necessary to pull data from firebase
    }

    public ItemEntry(String username, Status status, String item, String location, String description, @Nullable String imagePath) {
        this.username = username;
        this.item = item;
        this.status = status;
        this.location = location;
        this.description = description;
        this.date_created = FireBaseUtils.getStringFormattedDate();
        this.imagePath = imagePath;
    }

    private ItemEntry(ItemEntryBuilder itemEntryBuilder) {
        this.username = itemEntryBuilder.username;
        this.item = itemEntryBuilder.item;
        this.status = itemEntryBuilder.status;
        this.location = itemEntryBuilder.location;
        this.description = itemEntryBuilder.description;
        this.date_created = FireBaseUtils.getStringFormattedDate();
        this.imagePath = itemEntryBuilder.imagePath;
    }

    //region GETTERS necessary to push to firebase
    public String getUsername() { return this.username; }

    public Status getStatus() { return this.status; }

    public String getDate_created() { return this.date_created; }

    public String getDate_updated() { return this.date_updated; }

    public String getItem() { return this.item; }

    public String getLocation() { return this.location; }

    public String getDescription() { return this.description; }

    public String getImagePath() { return this.imagePath; }
    //endregion


    public void resolve() {
        this.date_updated = FireBaseUtils.getStringFormattedDate();
        this.status = Status.RESOLVED;
    }

    public void expire() {
        this.date_updated = FireBaseUtils.getStringFormattedDate();
        this.status = Status.EXPIRED;
    }

    public void update(String item, String description, String location) {
        this.date_updated = FireBaseUtils.getStringFormattedDate();
        this.item = item;
        this.location = location;
        this.description = description;
    }

    // IDK if yall wanna use builder design pattern when creating new Entry instead so here it is
    static class ItemEntryBuilder {
        private String username;
        private Status status;
        private String item;
        private String location;
        private String description;
        @Nullable private String imagePath;

        ItemEntryBuilder() {}

        public ItemEntryBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ItemEntryBuilder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public ItemEntryBuilder setItem(String item) {
            this.item = item;
            return this;
        }

        public ItemEntryBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public ItemEntryBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemEntryBuilder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public ItemEntry build() {
            return new ItemEntry(this);
        }
    }

}
