package com.example.ingredienttestapp;

public class Ingredient {
    private int id;
    private String name;
    private int imageId;

    public Ingredient(int id){
        this.id=id;
        set_name_image(id);
    }

    private void set_name_image(int id){
        switch(id){
            case 0:
                this.name="carrot";
                this.imageId=R.drawable.carrot;
                break;
            case 1:
                this.name="potato";
                this.imageId=R.drawable.potato;
                break;
            case 2:
                this.name="onion";
                this.imageId=R.drawable.onion;
                break;
            case 3:
                this.name="cabbage";
                this.imageId=R.drawable.cabbage;
                break;
            case 4:
                this.name="tomato";
                this.imageId=R.drawable.tomato;
                break;
            default:
                this.name="invalid";
                this.imageId=R.drawable.placeholder;
                break;
        }
    }

    public int get_image(){
        return this.imageId;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public boolean equals(Object o){
        if (this==o) {
            return true;
        }

        if (o==null||this.getClass()!=o.getClass()){
            return false;
        }

        Ingredient object=(Ingredient) o;
        return this.id==object.id;
    }
}
