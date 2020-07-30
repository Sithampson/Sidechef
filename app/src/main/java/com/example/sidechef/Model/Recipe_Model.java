package com.example.sidechef.Model;

import com.google.firebase.Timestamp;

public class Recipe_Model {
    private String RecipeName;
    private String MealType;
    private String Cuisine;
    private String RecipeDescription;
    private String Ingredients;
    private String RecipeProcedure;
    private String ImageUrl;
    private String RecipeChef;
    private String UserId;
    private Timestamp timestamp;
    private Integer ViewCount;

    public Recipe_Model() {
    }

    public Recipe_Model(String recipeName, String mealType, String cuisine, String recipeDescription, String ingredients, String recipeProcedure, String imageUrl, String recipeChef, String userId, Timestamp timestamp, Integer viewCount) {
        RecipeName = recipeName;
        MealType = mealType;
        Cuisine = cuisine;
        RecipeDescription = recipeDescription;
        Ingredients = ingredients;
        RecipeProcedure = recipeProcedure;
        ImageUrl = imageUrl;
        RecipeChef = recipeChef;
        UserId = userId;
        this.timestamp = timestamp;
        ViewCount = viewCount;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getMealType() {
        return MealType;
    }

    public void setMealType(String mealType) {
        MealType = mealType;
    }

    public String getCuisine() {
        return Cuisine;
    }

    public void setCuisine(String cuisine) {
        Cuisine = cuisine;
    }

    public String getRecipeDescription() {
        return RecipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        RecipeDescription = recipeDescription;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getRecipeProcedure() {
        return RecipeProcedure;
    }

    public void setRecipeProcedure(String recipeProcedure) {
        RecipeProcedure = recipeProcedure;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getRecipeChef() {
        return RecipeChef;
    }

    public void setRecipeChef(String recipeChef) {
        RecipeChef = recipeChef;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getViewCount() {
        return ViewCount;
    }

    public void setViewCount(Integer viewCount) {
        ViewCount = viewCount;
    }
}
