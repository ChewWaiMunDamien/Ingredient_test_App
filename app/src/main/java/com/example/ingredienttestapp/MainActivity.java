package com.example.ingredienttestapp;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private int max_size=3;
    private Inventory inventory;
    private IngredientFetchWorker worker;
    private List<Ingredient> genList=new ArrayList<>(max_size);

    private ImageView[] generatedViews=new ImageView[max_size];
    private ImageView[] inventoryViews=new ImageView[max_size];

    private Button fetchBtn;

    private VibratorManager vibe_manager;
    private VibrationEffect vibe_effect_default;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibe_manager=(VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        vibe_effect_default=VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE);

        inventory=new Inventory();

        worker = new IngredientFetchWorker();

        fetchBtn=findViewById(R.id.btn_generate);
        fetchBtn.setOnClickListener(v->{
            fetchBtn.setEnabled(false);
            int needed=max_size-inventory.getHeld().size();
            worker.generate_ingredients_random(needed,this::updateGeneratedUI);
        });

        init_imageViews();
    }

    private void init_imageViews(){
        inventoryViews[0]=findViewById(R.id.inv_1);
        inventoryViews[0].setImageResource(R.drawable.placeholder);
        inventoryViews[1]=findViewById(R.id.inv_2);
        inventoryViews[1].setImageResource(R.drawable.placeholder);
        inventoryViews[2]=findViewById(R.id.inv_3);
        inventoryViews[2].setImageResource(R.drawable.placeholder);

        for (int i=0;i<inventoryViews.length;i++){
            final int index = i;
            inventoryViews[i].setOnClickListener(v->{
                if (!inventory.isEmpty()){
                    List<Ingredient> inv=inventory.getHeld();
                    if (genList.size()<3){
                        Ingredient ing=inv.get(index);
                        genList.add(ing);
                        inventory.drop_by_index(index);

                        updateGenListUI();
                        updateInventoryUI();
                    }else{
                        vibe_manager.getDefaultVibrator().vibrate(vibe_effect_default);
                    }
                }
            });
        }

        generatedViews[0]=findViewById(R.id.gen_1);
        generatedViews[0].setImageResource(R.drawable.placeholder);
        generatedViews[1]=findViewById(R.id.gen_2);
        generatedViews[1].setImageResource(R.drawable.placeholder);
        generatedViews[2]=findViewById(R.id.gen_3);
        generatedViews[2].setImageResource(R.drawable.placeholder);

        for (int i=0;i<generatedViews.length;i++){
            final int index = i;
            generatedViews[i].setOnClickListener(v->{
                if (!genList.isEmpty()){
                    if (index<genList.size()){
                        Ingredient ing=genList.get(index);

                        if (!inventory.isFull()){
                            inventory.grabIngredient(ing);
                            genList.remove(index);

                            updateInventoryUI();
                            updateGenListUI();
                        }else{
                            vibe_manager.getDefaultVibrator().vibrate(vibe_effect_default);
                        }
                    }
                }
            });
        }
    }

    private void updateGeneratedUI(List<Ingredient> newList){
        runOnUiThread(()->{
            this.genList=newList;
            for (int i=0;i<generatedViews.length;i++){
                if (i<genList.size()){
                    generatedViews[i].setImageResource(genList.get(i).get_image());
                }else{
                    generatedViews[i].setImageResource(R.drawable.placeholder);
                }
            }
            fetchBtn.setEnabled(true);
        });
    }

    private void updateGenListUI(){
            for (int i=0;i<generatedViews.length;i++){
                if (i<genList.size()){
                    generatedViews[i].setImageResource(genList.get(i).get_image());
                }else{
                    generatedViews[i].setImageResource(R.drawable.placeholder);
                }
            }
    }

    private void updateInventoryUI(){
        List<Ingredient> inv=inventory.getHeld();
        for (int i=0;i<inventoryViews.length;i++){
            if (i<inv.size()){
                inventoryViews[i].setImageResource(inv.get(i).get_image());
            }else{
                inventoryViews[i].setImageResource(R.drawable.placeholder);
            }
        }
    }

}