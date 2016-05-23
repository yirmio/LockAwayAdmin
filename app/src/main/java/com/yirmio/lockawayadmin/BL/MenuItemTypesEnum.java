package com.yirmio.lockawayadmin.BL;

/**
 * Created by yirmio on 2/10/2015.
 */
public enum MenuItemTypesEnum {
    Drinks,
    Cakes,
    Extras,
    Specials,
    OnSale,
    Sandwiches,
    Salads,
    BreakFasts;

    public static String[] getAllValues(){
        String[] res = new String[MenuItemTypesEnum.values().length];
        MenuItemTypesEnum[] r = MenuItemTypesEnum.values();
        for (int i = 0; i < MenuItemTypesEnum.values().length; i++) {
            res[i] = r[i].name();
        }
        return res;
    }

    public static MenuItemTypesEnum getTypeFromInt(int position) {
        switch (position) {
            case 0:
                return Drinks;

            case 1:
                return Cakes;

            case 2:
                return Extras;

            case 3:
                return Specials;

            case 4:
                return OnSale;

            case 5:
                return Sandwiches;

            case 6:
                return Salads;

            case 7:
                return BreakFasts;

            default:
                return null;

        }
    }

}