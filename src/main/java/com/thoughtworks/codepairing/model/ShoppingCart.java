package com.thoughtworks.codepairing.model;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCart {
    /**
     * 这里可以改为用map存放 可以快速得到每个商品对应的个数
     */
    private List<Product> products;
    private Customer customer;

    public ShoppingCart(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * 计算购物车的当中商品的价值 并返回一个最后的订单实体
     *
     * 打折优惠规则：
     *  当产品不在任何优惠范围内时，忠诚度积分可以获得更多。
     *  顾客每购买5美元的产品就可获得1分。
     *  顾客每购买10美元的产品并享受10%的折扣，就可获得1个积分。
     *  顾客每购买15美元的产品并享受15%的折扣，就可获得1个积分。
     * @return
     */
    public Order checkout() {
        double totalPrice = 0;
        // 这里有点硬编码了 可以修改为按照DIS_10转换为一个数值
        int loyaltyPointsEarned = 0;
        for (Product product : products) {
            double discount = 0;
            if (product.getProductCode().startsWith("DIS_10")) {
                discount = (product.getPrice() * 0.1);
                loyaltyPointsEarned += (product.getPrice() / 10);
            } else if (product.getProductCode().startsWith("DIS_15")) {
                discount = (product.getPrice() * 0.15);
                loyaltyPointsEarned += (product.getPrice() / 15);
            } else {
                loyaltyPointsEarned += (product.getPrice() / 5);
            }

            totalPrice += product.getPrice() - discount;
        }

        return new Order(totalPrice, loyaltyPointsEarned);
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() + "\n" + "Bought:  \n" + products.stream().map(p -> "- " + p.getName()+ ", "+p.getPrice()).collect(Collectors.joining("\n"));
    }
}
