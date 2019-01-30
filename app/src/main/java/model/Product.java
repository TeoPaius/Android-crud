package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject implements Serializable {
    public Integer id;
    public Integer quantity;
    public Integer price;
    public String name;
    public String status;
    public String description;

    public static Product fromJson(JSONObject obj) throws JSONException {
        Product p = new Product();
        p.id = obj.getInt("id");
        p.quantity = obj.getInt("quantity");
        p.price = obj.getInt("price");
        p.name = obj.getString("name");
        p.status = obj.getString("status");
        p.description = obj.getString("description");
        return p;
    }

}
