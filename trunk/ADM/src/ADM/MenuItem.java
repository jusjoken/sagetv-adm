/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ADM;

/**
 *
 * @author bir0012
 */

//import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import sagex.UIContext;


public class MenuItem {
    public static final String SagePropertyLocation = "ADM/menuitem/";
    private String Parent = "";
    private String Name = "";
    private String ButtonText = "";
    private String SubMenu = "";
    private String Action = "";
    private String ActionType = "";
    private String BGImageFile = "";
    private String BGImageFilePath = "";
    private Boolean IsDefault = false;
    private Boolean IsActive = true;
    private Integer SortKey = 0;
    private static Integer SortKeyCounter = 0;
    public static Map<String,MenuItem> MenuItemList = new LinkedHashMap<String,MenuItem>();

    public MenuItem(String bName){
        //create a MenuItem with just default values
        Parent = "xTopMenu";
        Name = bName;
        ButtonText = "<Not defined>";
        SubMenu = null;
        ActionType = null;
        Action = null;
        SetBGImageFileandPath(null);
        IsDefault = false;
        IsActive = true;
        SortKey = 0;
        AddMenuItemtoList(this);
        
    }
    
    public MenuItem(String bParent, String bName, Integer bSortKey, String bButtonText, String bSubMenu, String bActionType, String bAction, String bBGImageFile, Boolean bIsDefault, Boolean bIsActive){
        Parent = bParent;
        Name = bName;
        ButtonText = bButtonText;
        SubMenu = bSubMenu;
        ActionType = bActionType;
        Action = bAction;
        SetBGImageFileandPath(bBGImageFile);
        IsDefault = bIsDefault;
        IsActive = bIsActive;
        SortKey = bSortKey;
        AddMenuItemtoList(this);
        //MenuItemList.put(this.Name, this);
        
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String ActionType) {
        this.ActionType = ActionType;
    }

    public String getBGImageFile() {
        return BGImageFile;
    }

    public void setBGImageFile(String BGImageFile) {
        SetBGImageFileandPath(BGImageFile);
    }

//    public String getBGImageFilePath() {
//        return BGImageFilePath;
//    }
//
//    public void setBGImageFilePath(String BGImageFilePath) {
//        this.BGImageFilePath = BGImageFilePath;
//    }
//
    public String getButtonText() {
        return ButtonText;
    }

    public void setButtonText(String ButtonText) {
        this.ButtonText = ButtonText;
    }

    public Boolean getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(Boolean IsDefault) {
        this.IsDefault = IsDefault;
    }

    public void setIsDefault(String IsDefault) {
        if ("true".equals(IsDefault)){
            this.IsDefault = true;
        }else{
            this.IsDefault = false;
        }
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    public void setIsActive(String IsActive) {
        if ("true".equals(IsActive)){
            this.IsActive = true;
        }else{
            this.IsActive = false;
        }
    }

    public static Map<String, MenuItem> getMenuItemList() {
        return MenuItemList;
    }

    public static void setMenuItemList(Map<String, MenuItem> MenuItemList) {
        MenuItem.MenuItemList = MenuItemList;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String Parent) {
        this.Parent = Parent;
    }

    public Integer getSortKey() {
        return SortKey;
    }

    public void setSortKey(String SortKey) {
        try {
            this.SortKey = Integer.valueOf(SortKey);
        } catch (NumberFormatException ex) {
            System.out.println("ADM: setSortKey: error converting '" + SortKey + "' " + util.class.getName() + ex);
            this.SortKey = SortKeyCounter++;
        }
    }

    public String getSubMenu() {
        return SubMenu;
    }

    public void setSubMenu(String SubMenu) {
        this.SubMenu = SubMenu;
    }
    
    final void AddMenuItemtoList(MenuItem NewMenuItem){
        MenuItemList.put(NewMenuItem.Name, NewMenuItem);
    }
    
    private void SetBGImageFileandPath(String bBGImageFile){
        //see if using a GlobalVariable from a Theme or a path to an image file
        if (bBGImageFile==null){
            System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - null found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = bBGImageFile;
        }else if (bBGImageFile.contains("\\")){
            //a path to the image file is being used
            System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - path found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = bBGImageFile;
        }else{
            //expect a Global Variable from the theme
            System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - variable found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = sagex.api.WidgetAPI.EvaluateExpression(new UIContext(sagex.api.Global.GetUIContextName()), bBGImageFile).toString();
        }
        
    }
    
    //returns the full list of ALL menu items regardless of parent
    public static Set<String> GetMenuItemNameList(){
        return MenuItemList.keySet();
    }
    
    //returns only menu items for a specific parent that are active
    public static Collection<String> GetMenuItemNameList(String Parent){
        SortedMap<Integer,String> bParentList = new TreeMap<Integer,String>();
        Collection<String> bSortedNames = new LinkedHashSet<String>();
        
        Iterator<Entry<String,MenuItem>> itr = MenuItemList.entrySet().iterator(); 
        while (itr.hasNext()) {
            Entry<String,MenuItem> entry = itr.next();
            //check for the correct parent
            if (entry.getValue().Parent == null ? Parent == null : entry.getValue().Parent.equals(Parent)){
                //only select Active MenuItems
                if (entry.getValue().IsActive==true){
                    bParentList.put(entry.getValue().SortKey,entry.getValue().Name);
                }
            }
        }         
        bSortedNames = bParentList.values();
        System.out.println("ADM: GetMenuItemNameList for '" + Parent + "' :" + bSortedNames);
        
        return bSortedNames;
    }
    
    public static String GetMenuItemParent(String Name){
        return MenuItemList.get(Name).Parent;
    }

    public static String GetMenuItemButtonText(String Name){
        return MenuItemList.get(Name).ButtonText;
    }

    public static Integer GetMenuItemSortKey(String Name){
        return MenuItemList.get(Name).SortKey;
    }

    public static String GetMenuItemSubMenu(String Name){
        return MenuItemList.get(Name).SubMenu;
    }

    public static String GetMenuItemAction(String Name){
        return MenuItemList.get(Name).Action;
    }

    public static String GetMenuItemActionType(String Name){
        return MenuItemList.get(Name).ActionType;
    }

    public static String GetMenuItemBGImageFilePath(String Name){
        return MenuItemList.get(Name).BGImageFilePath;
    }

    public static String GetMenuItemBGImageFile(String Name){
        return MenuItemList.get(Name).BGImageFile;
    }

    public static Boolean GetMenuItemIsDefault(String Name){
        return MenuItemList.get(Name).IsDefault;
    }

    public static Boolean GetMenuItemIsActive(String Name){
        return MenuItemList.get(Name).IsActive;
    }

    public static int GetMenuItemCount(){
        return MenuItemList.size();
    }
    
//    public static int GetMenuItemCount(String Parent){
//        SortedMap<Integer,String> bParentList = new TreeMap<Integer,String>();
//        Collection<String> bSortedNames = new LinkedHashSet<String>();
//        
//        Iterator<Entry<String,MenuItem>> itr = MenuItemList.entrySet().iterator(); 
//        while (itr.hasNext()) {
//            Entry<String,MenuItem> entry = itr.next();
//            //check for the correct parent
//            if (entry.getValue().Parent == null ? Parent == null : entry.getValue().Parent.equals(Parent)){
//                //only select Active MenuItems
//                if (entry.getValue().IsActive){
//                    bParentList.put(entry.getValue().SortKey,entry.getValue().Name);
//                }
//            }
//        }         
//        bSortedNames = bParentList.values();
//        System.out.println("ADM: GetMenuItemCount for '" + Parent + "' :" + bSortedNames.size());
//        
//        return bSortedNames.size();
//    }

    //Get the count of MenuItems for a parent that are active
    public static int GetMenuItemCount(String Parent){
        Collection<String> bSortedNames = GetMenuItemNameList(Parent);
        System.out.println("ADM: GetMenuItemCount for '" + Parent + "' :" + bSortedNames.size());
        return bSortedNames.size();
    }
    
}
