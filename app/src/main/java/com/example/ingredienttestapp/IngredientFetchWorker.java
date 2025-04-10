package com.example.ingredienttestapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IngredientFetchWorker{
    private ExecutorService executor= Executors.newSingleThreadExecutor();
    private int max_count=3;
    private int total_count=5;
    private List<Ingredient> allIngredientList=new ArrayList<>(total_count);
    private List<Ingredient> activeList=new ArrayList<>(max_count);

    private List<Ingredient> availableList=new ArrayList<>(total_count);
    private int gen_time=3000;
    Random random;

    public interface generator_listener{
        void onGenerated(List<Ingredient> newIngredients);
    }

    public IngredientFetchWorker(){
        generateIngredientList();
        this.random=new Random();
    }

    private void generateIngredientList(){
        for (int i=0;i<total_count;i++){
            Ingredient ingredient=new Ingredient(i);
            this.allIngredientList.add(ingredient);
        }
        this.availableList=new ArrayList<>(allIngredientList);
    }

    public List<Ingredient> getActiveList(){
        return activeList;
    }

    private void generateRandomStartIngredients(){
        for (int i=0;i<max_count;i++){
            int index=random.nextInt(total_count);
            activeList.add(allIngredientList.get(index));
        }
    }

    public void generate_ingredients(List<Integer> genList,generator_listener listener){
        executor.submit(()->{
            try{
                Thread.sleep(gen_time);
            }catch(InterruptedException e){
                //handle later
            }

            List<Ingredient> result=new ArrayList<>();
            for (int i=0;i<genList.size();i++){
                result.add(allIngredientList.get(genList.get(i)));
            }
            listener.onGenerated(result);
        });
    }

    public void generate_ingredients_random(Integer get_number,generator_listener listener){
        executor.submit(()->{
            fetch_ingredients_random(get_number,listener);
        });
    }

    private void fetch_ingredients_random(Integer get_number,generator_listener listener){
        try{
            Thread.sleep(gen_time);
        }catch(InterruptedException e){
            //handle later
        }
        int start=random.nextInt(total_count-get_number);
        List<Ingredient> result=new ArrayList<>(allIngredientList.subList(start,get_number+start));
        listener.onGenerated(result);
    }

    private List<Ingredient> exchangeActiveList(List<Integer> removeIds,List<Integer> addIds){
        if (addIds.size()!=removeIds.size()){
            return null;
        }

        for (int i=0;i<removeIds.size();i++){
            activeList.remove(allIngredientList.get(removeIds.get(i)));
        }

        for (int i=0;i<addIds.size();i++){
            activeList.add(allIngredientList.get(addIds.get(i)));
        }
        return activeList;
    }
}
