package com.capston.chatting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;
    private int price;
    private String comment;

    public Item(String itemName, int price, String comment) {
        this.itemName = itemName;
        this.price = price;
        this.comment = comment;
    }
}
