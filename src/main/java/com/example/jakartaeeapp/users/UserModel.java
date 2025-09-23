package com.example.jakartaeeapp.users;

public class UserModel {
    private Integer id;
    private String name;
    private Integer age;

    private UserModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
    }

    public static class Builder {
        private int id;
        private String name;
        private Integer age;

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder age(Integer age) { this.age = age; return this; }

        public UserModel build() {
            return new UserModel(this);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
