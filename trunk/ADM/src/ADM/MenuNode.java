/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ADM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author jusjoken
 */
public class MenuNode {
    public static DefaultMutableTreeNode Testing;
    private static final Random random = new Random();

    private String Parent = "";
    public String Name = "";
    private String ButtonText = "";
    private String SubMenu = "";
    private String ActionAttribute = "";
    private String ActionType = "";
    private String BGImageFile = "";
    private String BGImageFilePath = "";
    private Boolean IsDefault = false;
    private Boolean IsActive = true;
    private Boolean HasSubMenu = false;
    private Integer SortKey = 0;
    private Integer Level = 0;
    private DefaultMutableTreeNode NodeItem;
    public static Integer SortKeyCounter = 0;
    public static Map<String,MenuNode> MenuNodeList = new LinkedHashMap<String,MenuNode>();
    public static DefaultMutableTreeNode root = new DefaultMutableTreeNode(util.OptionNotFound);

    public MenuNode(String bName){
        //create a MenuItem with just default values
        this(util.TopMenu,bName,0,util.ButtonTextDefault,Boolean.FALSE,null,util.ActionTypeDefault,null,null,Boolean.FALSE,Boolean.TRUE);
    }
    
    public MenuNode(String bParent, String bName, Integer bSortKey, String bButtonText, Boolean bHasSubMenu, String bSubMenu, String bActionType, String bAction, String bBGImageFile, Boolean bIsDefault, Boolean bIsActive){
        Parent = bParent;
        Name = bName;
        ButtonText = bButtonText;
        SubMenu = bSubMenu;
        HasSubMenu = bHasSubMenu;
        ActionType = bActionType;
        ActionAttribute = bAction;
        SetBGImageFileandPath(bBGImageFile);
        IsDefault = bIsDefault;
        IsActive = bIsActive;
        SortKey = bSortKey;
        MenuNodeList.put(Name, this);
    }
    
    @Override
    public String toString(){
        return Name;
    }

    public static String GetMenuItemAction(String Name){
        //System.out.println("ADM: GetMenuItemAction for '" + Name + "' = '" + MenuNodeList.get(Name).ActionAttribute + "'");
        return MenuNodeList.get(Name).ActionAttribute;
    }

    public static void SetMenuItemAction(String Name, String Setting){
        if (GetMenuItemActionType(Name).equals(Action.BrowseVideoFolder)){
            //ensure the Folder string ends in a "/" unless it's blank
            if (Setting.isEmpty() || !Setting.endsWith("/")){
                Setting = Setting + "/";
            }
            if (Setting.equals("/")){
                Setting = null;
            }
        }
        Save(Name, "Action", Setting);
    }

    public static String GetMenuItemActionType(String Name){
        return MenuNodeList.get(Name).ActionType;
    }

    public static void SetMenuItemActionType(String Name, String Setting){
        Save(Name, "ActionType", Setting);
    }
    
    private void SetBGImageFileandPath(String bBGImageFile){
        //see if using a GlobalVariable from a Theme or a path to an image file
        if (bBGImageFile==null || bBGImageFile.equals("")){
            //System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - null found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = bBGImageFile;
        }else if (bBGImageFile.contains(File.separator)){
            //a path to the image file is being used
            //System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - path found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = bBGImageFile;
        }else{
            //expect a Global Variable from the theme
            //System.out.println("ADM: SetBGImageFileandPath for '" + bBGImageFile + "' - variable found");
            BGImageFile = bBGImageFile;
            BGImageFilePath = sagex.api.WidgetAPI.EvaluateExpression(util.GetMyUIContext(), bBGImageFile).toString();
        }
    }
    
    public static String GetMenuItemBGImageFileButtonText(String Name){
        return util.GetSageBGVariablesButtonText(MenuNodeList.get(Name).BGImageFile);
    }
    
    public static String GetMenuItemBGImageFile(String Name){
        return MenuNodeList.get(Name).BGImageFile;
    }

    public static String GetMenuItemBGImageFilePath(String Name){
        return MenuNodeList.get(Name).BGImageFilePath;
    }

    public static void SetMenuItemBGImageFile(String Name, String Setting){
        if (Setting.equals(util.ListNone) || Setting==null){
            Save(Name, "BGImageFile", null);
        }else{
            Save(Name, "BGImageFile", Setting);
        }
    }

    public static String GetMenuItemButtonText(String Name){
        if (Name.equals(util.TopMenu)){
            return "Top Level";
        }else{
            return MenuNodeList.get(Name).ButtonText;
        }
    }

    public static String GetMenuItemButtonTextNewTest(String Name){
        if (MenuNodeList.get(Name).ButtonText.equals(util.ButtonTextDefault)){
            return "";
        }else{
            return MenuNodeList.get(Name).GetMenuItemButtonText(Name);
        }
    }

    public static void SetMenuItemButtonText(String Name, String Setting){
        Save(Name, "ButtonText", Setting);
    }

    public static Boolean GetMenuItemHasChildren(String Name){
        return !MenuNodeList.get(Name).NodeItem.isLeaf();
    }

    public static Boolean GetMenuItemHasSubMenu(String Name){
        return MenuNodeList.get(Name).HasSubMenu;
    }

    public static void SetMenuItemHasSubMenu(String Name, Boolean Setting){
        Save(Name, "HasSubMenu", Setting.toString());
    }

    public static Boolean GetMenuItemIsActive(String Name){
        return MenuNodeList.get(Name).IsActive;
    }

    public static String GetMenuItemIsActiveIncludingParentFormatted(String Name){
        if (!MenuNodeList.get(Name).IsActive){
            return "No";
        }else if (GetMenuItemIsActiveIncludingParent(Name)){
            return "Yes";
        }
        // in this case this item is active BUT the parent is not
        return "Yes (but Parent is not Active)";
    }
    
    public static Boolean GetMenuItemIsActiveIncludingParent(String Name){
        TreeNode[] path = MenuNodeList.get(Name).NodeItem.getPath();
        for (TreeNode pathnode : path){
            DefaultMutableTreeNode pathnodea = (DefaultMutableTreeNode)pathnode;
            MenuNode tMenu = (MenuNode)pathnodea.getUserObject();
            if (!tMenu.IsActive){
                System.out.println("ADM: GetMenuItemIsActiveIncludingParent for '" + Name + "' NOTACTIVE for item = '" + tMenu.Name + "'");
                return Boolean.FALSE;
            }
        }
        System.out.println("ADM: GetMenuItemIsActiveIncludingParent for '" + Name + "' ISACTIVE");
        return Boolean.TRUE;
    }

    public static void SetMenuItemIsActive(String Name, Boolean Setting){
        Save(Name, "IsActive", Setting.toString());
    }

    public static Boolean GetMenuItemIsDefault(String Name){
        return MenuNodeList.get(Name).IsDefault;
    }

    public static void SetMenuItemIsDefault(String Name, Boolean Setting){
        //System.out.println("ADM: SetMenuItemIsDefault: Name '" + Name + "' Setting '" + Setting + "'");
        if (Setting==Boolean.TRUE){
            //System.out.println("ADM: SetMenuItemIsDefault: true Name '" + Name + "' Setting '" + Setting + "'");
            //first clear existing Default settings for Menu Items with the same parent 
            ClearSubMenuDefaults(MenuNodeList.get(Name).Parent);
            Save(Name, "IsDefault", Setting.toString());
        }else{
            //System.out.println("ADM: SetMenuItemIsDefault: false Name '" + Name + "' Setting '" + Setting + "'");
            Save(Name, "IsDefault", Setting.toString());
            //ensure at least 1 item remaining is a default
            ValidateSubMenuDefault(MenuNodeList.get(Name).Parent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void ValidateSubMenuDefault(String bParent){
        //ensure that 1 and only 1 item is set as the default
        Boolean FoundDefault = Boolean.FALSE;
        
        if (MenuNodeList.get(bParent).NodeItem.getChildCount()>0){

            Enumeration<DefaultMutableTreeNode> en = MenuNodeList.get(bParent).NodeItem.preorderEnumeration();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsDefault){
                    if (FoundDefault){
                    //Save setting
                        Save(tMenu.Name, "IsDefault", Boolean.FALSE.toString());
                    }else{
                        FoundDefault = Boolean.TRUE;
                    }
                }
            }         
            if (!FoundDefault){
                //no default found so set the first item as the default
                DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)MenuNodeList.get(bParent).NodeItem.getFirstChild();
                MenuNode tMenu = (MenuNode)firstChild.getUserObject();
                System.out.println("ADM: ValidateSubMenuDefault for '" + bParent + "' : no Default found so setting first = '" + tMenu.Name + "'");
                Save(tMenu.Name, "IsDefault", Boolean.TRUE.toString());
            }else {
                System.out.println("ADM: ValidateSubMenuDefault for '" + bParent + "' : Default already set");
            }
        }else{
            //no subMenu items so make sure this parent's SubMenu settings are correct
            SetMenuItemSubMenu(bParent, util.ListNone);
            SetMenuItemHasSubMenu(bParent, Boolean.FALSE);
            System.out.println("ADM: ValidateSubMenuDefault for '" + bParent + "' : no SubMenu items found");
        }
    }
        
    //clear all defaults for a submenu - used prior to setting a new default to ensure there is not more than one
    @SuppressWarnings("unchecked")
    public static void ClearSubMenuDefaults(String bParent){
        if (MenuNodeList.get(bParent).NodeItem.getChildCount()>0){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList.get(bParent).NodeItem.preorderEnumeration();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsDefault){
                    //Save setting
                    Save(tMenu.Name, "IsDefault", Boolean.FALSE.toString());
                }
            }         
        }
        System.out.println("ADM: ClearSubMenuDefaults for '" + bParent + "' '" + MenuNodeList.get(bParent).NodeItem.getChildCount() + "' cleared");
    }
    
    @SuppressWarnings("unchecked")
    public static String GetSubMenuDefault(String bParent){
        if (MenuNodeList.get(bParent).NodeItem.getChildCount()>0){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList.get(bParent).NodeItem.preorderEnumeration();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsDefault){
                    System.out.println("ADM: GetSubMenuDefault for '" + bParent + "' Default = '" + tMenu.Name + "'");
                    return tMenu.Name;
                }
            }         
        }
        System.out.println("ADM: GetSubMenuDefault for '" + bParent + "' - none found");
        return "";
    }

    public static Integer GetMenuItemLevel(String Name){
        return MenuNodeList.get(Name).NodeItem.getLevel();
    }

    public static void SetMenuItemName(String Name){
        Save(Name, "Name", Name);
    }

    public static String GetMenuItemParent(String Name){
        //get the parent from the Tree structure
        System.out.println("ADM: GetMenuItemParent for '" + Name + "'");
        System.out.println("ADM: GetMenuItemParent for '" + Name + "' = '" + MenuNodeList.get(Name).NodeItem.getParent().toString() + "'");
        return MenuNodeList.get(Name).NodeItem.getParent().toString();
    }

    // Should not need to set the parent attribute as the Tree takes care of this
//    public static void SetMenuItemParent(String Name, String Setting){
//        String OldParent = MenuItemList.get(Name).getParent();
//        SaveMenuItemtoSage(Name, "Parent", Setting);
//        MenuItemList.get(Name).setParent(Setting);
//
//        //see if the Level needs changing
//        if (GetMenuItemLevel(OldParent)==GetMenuItemLevel(Setting)){
//            //Same levels so no need to change
//        }else{
//            //set level to 1 more than the parent
//            SetMenuItemLevel(Name,GetMenuItemLevel(Setting)+1);
//        }
//        
//        if (OldParent.equals(Setting) || OldParent==null || Setting.equals(util.TopMenu)){
//            System.out.println("ADM: SetMenuItemParent: Parent saved for '" + Name + "' to = '" + Setting + "' OldParent = '" + OldParent + "'");
//        }else{
//            //check the new parent and set it's SubMenu properly
//            SetMenuItemSubMenu(Setting,util.ListNone);
//            SetMenuItemHasSubMenu(Setting, Boolean.TRUE);
//
//            //make sure the old and new SubMenus have a single default item
//            ValidateSubMenuDefault(OldParent);
//            ValidateSubMenuDefault(Setting);
//            System.out.println("ADM: SetMenuItemParent: Parent changed for '" + Name + "' to = '" + Setting + "'");
//        }
//    }

    public static Integer GetMenuItemSortKey(String Name){
        return MenuNodeList.get(Name).SortKey;
    }

    public void setSortKey(String SortKey) {
        Integer tSortKey = 0;
        try {
            tSortKey = Integer.valueOf(SortKey);
        } catch (NumberFormatException ex) {
            System.out.println("ADM: setSortKey: error converting '" + SortKey + "' " + util.class.getName() + ex);
            tSortKey = SortKeyCounter++;
        }
        this.SortKey = tSortKey;
    }
    
    public static void SortKeyUpdate(){
        SortKeyUpdate(root);
    }
    
    @SuppressWarnings("unchecked")
    public static void SortKeyUpdate(DefaultMutableTreeNode aParent){
        //update all the sortkey values to the index starting from the aParent
        Enumeration<DefaultMutableTreeNode> en = aParent.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (!tMenu.Name.equals(util.TopMenu)){
                tMenu.SortKey = child.getParent().getIndex(child);
                String PropLocation = util.SagePropertyLocation + tMenu.Name + "/" + "SortKey";
                sagex.api.Configuration.SetProperty(PropLocation, tMenu.SortKey.toString());
                System.out.println("ADM: SortKeyUpdate: Child = '" + child + "' SortKey = '" + tMenu.SortKey + "' Parent = '" + child.getParent() + "'"  );
            }
        }         
    }
    
    //the SubMenu field is only filled in if using a built in Sage SubMenu
    // otherwise, the Name of the MenuItem is returned if the MenuItem has a SubMenu
    public String getSubMenu() {
        if (SubMenu==null){
            if (HasSubMenu){
                return Name;
            }else{
                return SubMenu;
            }
        }else{
            return SubMenu;
        }
    }

    public static String GetMenuItemSubMenu(String Name){
        return MenuNodeList.get(Name).getSubMenu();
    }

    public static String GetMenuItemSubMenuButtonText(String Name){
        return util.GetSubMenuListButtonText(MenuNodeList.get(Name).getSubMenu(), GetMenuItemLevel(Name));
    }

    public static void SetMenuItemSubMenu(String Name, String Setting){
        //System.out.println("ADM: SetMenuItemSubMenu for '" + Name + "' Setting = '" + Setting + "'");
        if (Setting.equals(util.ListNone) || Setting==null){
            //set the SubMenu field
            Save(Name, "SubMenu", null);
            //set the HasSubMenu field
            Save(Name, "HasSubMenu", Boolean.FALSE.toString());
        }else{
            //set the SubMenu field
            Save(Name, "SubMenu", Setting);
            //set the HasSubMenu field
            Save(Name, "HasSubMenu", Boolean.TRUE.toString());
        }
    }

    public static Boolean IsSubMenuItem(String bParent, String Item){
        //check if Item is a child of bParent
        //Collection<String> SubMenuItems = GetMenuItemNameList(bParent,Boolean.TRUE);
        if (MenuNodeList.get(Item).NodeItem.getParent().toString().equals(bParent)){
            System.out.println("ADM: IsSubMenuItem for Parent = '" + bParent + "' Item '" + Item + "' found");
            return Boolean.TRUE;
        }else{
            System.out.println("ADM: IsSubMenuItem for Parent = '" + bParent + "' Item '" + Item + "' NOT found");
            return Boolean.FALSE;
        }
    }

    //returns the full list of ALL menu items regardless of parent
    public static Collection<String> GetMenuItemNameList(){
        return GetMenuItemNameList(util.TopMenu);
    }
    
    //returns only menu items for a specific parent that are active
    public static Collection<String> GetMenuItemNameList(String Parent){
        return GetMenuItemNameList(Parent, Boolean.FALSE);
    }

    //returns menu items for a specific parent
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemNameList(String Parent, Boolean IncludeInactive){
        Collection<String> bNames = new LinkedHashSet<String>();
        if (MenuNodeList.containsKey(Parent)){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList.get(Parent).NodeItem.preorderEnumeration();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsActive==true || IncludeInactive==true){
                    bNames.add(tMenu.Name);
                }
            }         
        }
        System.out.println("ADM: GetMenuItemNameList for '" + Parent + "' : IncludeInactive = '" + IncludeInactive.toString() + "' " + bNames);
        return bNames;
    }
    
    //returns only menu items for a specific parent that are active
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemSortedList(Boolean Grouped){
        Collection<String> FinalList = new LinkedHashSet<String>();
        
        Enumeration<DefaultMutableTreeNode> en;
        if (Grouped){
            //Menu Items in Level 1, then Level 2 etc
            en = root.breadthFirstEnumeration();
        }else{
            //Menu Items in Tree Order
            en = root.preorderEnumeration();
        }
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            //add all items except the Top Level menu
            if (!tMenu.Name.equals(util.TopMenu)){
                FinalList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: GetMenuItemSortedList: Grouped = '" + Grouped.toString() + "' :" + FinalList);
        return FinalList;
    }

    public static Collection<String> GetParentListforMenuItem(String Name){
        System.out.println("ADMTemp: GetMenuItemSubMenu: for '" + Name + "' ='" + GetMenuItemSubMenu(Name) + "'");
        if (Name.equals(GetMenuItemSubMenu(Name)) || GetMenuItemSubMenu(Name)==null){
            return GetMenuItemParentList();
        }else{
            return GetMenuItemParentList(GetMenuItemLevel(Name));
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemParentList(){
        Collection<String> ValidParentList = new LinkedHashSet<String>();
        Enumeration<DefaultMutableTreeNode> en = root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (tMenu.Level<3){
                ValidParentList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: GetMenuItemParentList: '" + ValidParentList + "'");
        return ValidParentList;
    }
    
    //get valid parent list for only 1 specific level
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemParentList(Integer SpecificLevel){
        Collection<String> ValidParentList = new LinkedHashSet<String>();
        Enumeration<DefaultMutableTreeNode> en = root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (tMenu.Level==SpecificLevel-1){
                ValidParentList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: GetMenuItemParentList: for Level = '" + SpecificLevel + "' List = '" + ValidParentList + "'");
        return ValidParentList;
    }
    
    //get the specific format based on the Sort style
    public static String GetMenuItemButtonTextbyStyle(String Name, String SortStyle){
        String SubMenuText = GetMenuItemSubMenu(Name);
        if (SubMenuText!=null){
            if (SubMenuText.equals(Name)){
                SubMenuText = "";
            }else{
                SubMenuText = " <" + util.GetSubMenuListButtonText(SubMenuText, GetMenuItemLevel(Name),Boolean.TRUE) + ">";
            }
        }else{
            SubMenuText = "";
        }
        String DefaultIndicator = "";
        if (MenuNodeList.get(Name).IsDefault){
            DefaultIndicator = "* ";
        }
        if (SortStyle.equals(util.SortStyleDefault)){
            //return a prefix padded string
            return GetMenuItemButtonTextFormatted(Name,"     ") + DefaultIndicator + SubMenuText;
        }else{
            //return a / delimited path
            return GetMenuItemButtonTextFormatted(Name,null) + DefaultIndicator + SubMenuText;
        }
    }

    //return a path delimited by "/" or the name with prefix padding
    public static String GetMenuItemButtonTextFormatted(String Name, String PrefixPadding){
        if (PrefixPadding==null){
            return GetPath(MenuNodeList.get(Name).NodeItem);
        }else{
            String tPadded = "";
            if(MenuNodeList.get(Name).NodeItem.getLevel()==2){
                tPadded = PrefixPadding;
            }else if(MenuNodeList.get(Name).NodeItem.getLevel()==3){
                tPadded = PrefixPadding + PrefixPadding;
            }
            return tPadded + MenuNodeList.get(Name).ButtonText;
        }
    }

    public static int GetMenuItemCount(){
        return GetMenuItemCount(util.TopMenu);
    }
    
    //Get the count of MenuItems for a parent that are active
    public static int GetMenuItemCount(String Parent){
        Collection<String> bNames = GetMenuItemNameList(Parent);
        System.out.println("ADM: GetMenuItemCount for '" + Parent + "' :" + bNames.size());
        return bNames.size();
    }

    public static void Execute(String Name){
        Action.Execute(GetMenuItemActionType(Name), GetMenuItemAction(Name));
    }
    
    public static String GetActionAttributeButtonText(String Name){
        return Action.GetAttributeButtonText(GetMenuItemActionType(Name), GetMenuItemAction(Name));
    }

    public static void LoadMenuItemsFromSage(){
        String PropLocation = "";
        
        //find all MenuItem Name entries from the SageTV properties file
        String[] MenuItemNames = sagex.api.Configuration.GetSubpropertiesThatAreBranches(util.SagePropertyLocation);
        
        if (MenuItemNames.length>0){
            //clear the existing MenuItems from the list
            MenuNodeList.clear();
            root.removeAllChildren();

            //create and store the top menu node
            MenuNode rootNode = new MenuNode(util.TopMenu);
            root = new DefaultMutableTreeNode(rootNode);
            rootNode.NodeItem = root;
            rootNode.ButtonText = "Top Level";
            
            //load MenuItems
            for (String tMenuItemName : MenuItemNames){
                PropLocation = util.SagePropertyLocation + tMenuItemName;
                MenuNode NewMenuItem = new MenuNode(tMenuItemName);
                NewMenuItem.ActionAttribute = sagex.api.Configuration.GetProperty(PropLocation + "/Action", null);
                NewMenuItem.ActionType = sagex.api.Configuration.GetProperty(PropLocation + "/ActionType", util.ActionTypeDefault);
                NewMenuItem.SetBGImageFileandPath(sagex.api.Configuration.GetProperty(PropLocation + "/BGImageFile", null));
                NewMenuItem.ButtonText = sagex.api.Configuration.GetProperty(PropLocation + "/ButtonText", util.ButtonTextDefault);
                NewMenuItem.Name = sagex.api.Configuration.GetProperty(PropLocation + "/Name", tMenuItemName);
                NewMenuItem.Parent = sagex.api.Configuration.GetProperty(PropLocation + "/Parent", "xTopMenu");
                NewMenuItem.setSortKey(sagex.api.Configuration.GetProperty(PropLocation + "/SortKey", null));
                NewMenuItem.SubMenu = sagex.api.Configuration.GetProperty(PropLocation + "/SubMenu", null);
                NewMenuItem.HasSubMenu = Boolean.parseBoolean(sagex.api.Configuration.GetProperty(PropLocation + "/HasSubMenu", "false"));
                NewMenuItem.IsDefault = Boolean.parseBoolean(sagex.api.Configuration.GetProperty(PropLocation + "/IsDefault", "false"));
                NewMenuItem.IsActive = Boolean.parseBoolean(sagex.api.Configuration.GetProperty(PropLocation + "/IsActive", "true"));
                System.out.println("ADM: LoadMenuItemsFromSage: loaded - '" + tMenuItemName + "'");
            }
            if (MenuNodeList.size()>0){
        System.out.println("ADM: 6 LoadMenuItemsFromSage");
                //create the tree nodes
                for (MenuNode Node : MenuNodeList.values()){
                    //check if the current node exists yet
                    AddNode(Node);
                }
                //now update the sortkeys from the Tree structure
                
                //DONT RUN THIS TILL THIS NEW STUFF IS READY
                //SortKeyUpdate();
            }
            
        }else{
            //load a default Menu here.  Load a Diamond Menu if Diamond if active
            System.out.println("ADM: LoadMenuItemsFromSage: no MenuItems found - loading default menu.");
            //LoadMenuItemDefaults();
        }
        System.out.println("ADM: LoadMenuItemsFromSage: loaded " + MenuNodeList.size() + " MenuItems");
        
        //now that the menus are loaded - set a level for each menu item and store it
        //MenuItem.SetMenuItemLevels();
        //now ensure SortKeys are in order
        //FixSortOrder();
        
        return;
    }
    
    //saves all MenuItems to Sage properties
    @SuppressWarnings("unchecked")
    public static void SaveMenuItemsToSage(){
        String PropLocation = "";
        
        //clean up existing MenuItems from the SageTV properties file before writing the new ones
        sagex.api.Configuration.RemovePropertyAndChildren(util.SagePropertyLocation);
        //clear the MenuNodeList and rebuild it while saving
        MenuNodeList.clear();
        
        //iterate through all the MenuItems and save to SageTV properties
        Enumeration<DefaultMutableTreeNode> en = root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            PropLocation = util.SagePropertyLocation + tMenu.Name;
            sagex.api.Configuration.SetProperty(PropLocation + "/Action", tMenu.ActionAttribute);
            sagex.api.Configuration.SetProperty(PropLocation + "/ActionType", tMenu.ActionType);
            sagex.api.Configuration.SetProperty(PropLocation + "/BGImageFile", tMenu.BGImageFile);
            sagex.api.Configuration.SetProperty(PropLocation + "/ButtonText", tMenu.ButtonText);
            sagex.api.Configuration.SetProperty(PropLocation + "/Name", tMenu.Name);
            sagex.api.Configuration.SetProperty(PropLocation + "/Parent", tMenu.Parent);
            sagex.api.Configuration.SetProperty(PropLocation + "/SortKey", tMenu.SortKey.toString());
            sagex.api.Configuration.SetProperty(PropLocation + "/SubMenu", tMenu.SubMenu);
            sagex.api.Configuration.SetProperty(PropLocation + "/HasSubMenu", tMenu.HasSubMenu.toString());
            sagex.api.Configuration.SetProperty(PropLocation + "/IsDefault", tMenu.IsDefault.toString());
            sagex.api.Configuration.SetProperty(PropLocation + "/IsActive", tMenu.IsActive.toString());
            //add the item into the MenuNodeList
            MenuNodeList.put(tMenu.Name, tMenu);
        }         
        System.out.println("ADM: SaveMenuItemsToSage: saved " + MenuNodeList.size() + " MenuItems");
        
        return;
    }
 
    public static void DeleteMenuItem(String Name){
        //store the parent for later cleanup
        String OldParent = GetMenuItemParent(Name);
        //do all the deletes first
        MenuNodeList.get(Name).NodeItem.removeAllChildren();
        MenuNodeList.get(Name).NodeItem.removeFromParent();
        //Make sure there is still one default Menu Item
        ValidateSubMenuDefault(OldParent);
        //rebuild any lists
        SaveMenuItemsToSage();
        System.out.println("ADM: DeleteMenuItem: deleted '" + Name + "'");
    }
    
    public static void DeleteAllMenuItems(){

        //backup existing MenuItems before deleting
        if (MenuNodeList.size()>0){
            ExportMenuItems(util.PropertyBackupFile);
        }
        
        //clean up existing MenuItems from the SageTV properties file
        sagex.api.Configuration.RemovePropertyAndChildren(util.SagePropertyLocation);
        MenuNodeList.clear();
        root.removeAllChildren();
        
        //Create 1 new MenuItem at the TopMenu level
        NewMenuItem(util.TopMenu, 1) ;

        //now load the properties from the Sage properties file
        LoadMenuItemsFromSage();

        System.out.println("ADM: DeleteAllMenuItems: completed");
    }
    
    public static String NewMenuItem(String Parent, Integer SortKey){
        String tMenuItemName = GetNewMenuItemName();

        //Create a new MenuItem with defaults
        MenuNode NewMenuItem = new MenuNode(Parent,tMenuItemName,SortKey,util.ButtonTextDefault,Boolean.FALSE,util.ListNone,util.ActionTypeDefault,null,util.ListNone,Boolean.FALSE,Boolean.TRUE);
        //add the Node to the Tree
        InsertNode(MenuNodeList.get(Parent).NodeItem, NewMenuItem);
        
        System.out.println("ADM: NewMenuItem: created '" + tMenuItemName + "' SortKey = '" + SortKey + "'");
        return tMenuItemName;
    }
 
    public static void LoadMenuItemDefaults(){
        //load default MenuItems from one or more default .properties file
        String DefaultPropFile = "ADMDefault.properties";
        String DefaultPropFileDiamond = "ADMDefaultDiamond.properties";
        String DefaultsFullPath = util.ADMDefaultsLocation + File.separator + DefaultPropFile;
        String DiamondVideoMenuCheckProp = "JOrton/MainMenu/ShowDiamondMoviesTab";
        String DiamondMenuVideos = "admSageTVVideos";
        String DiamondMenuMovies = "admDiamondMovies";
        
        
        // check to see if the Diamond Plugin is installed
        if (Diamond.IsDiamond()){
            DefaultsFullPath = util.ADMDefaultsLocation + File.separator + DefaultPropFileDiamond;
        }
        ImportMenuItems(DefaultsFullPath);
        
        //for Diamond we need to Hide either the Videos Menu Item or the Movies Menu Item
        if (Diamond.IsDiamond()){
            //admSageTVVideos
            if ("true".equals(sagex.api.Configuration.GetProperty(DiamondVideoMenuCheckProp, "false"))){
                //show the Videos Menu
                SetMenuItemIsActive(DiamondMenuMovies, Boolean.TRUE);
                SetMenuItemIsActive(DiamondMenuVideos, Boolean.FALSE);
            }else{
                //show the Movies Menu
                SetMenuItemIsActive(DiamondMenuMovies, Boolean.FALSE);
                SetMenuItemIsActive(DiamondMenuVideos, Boolean.TRUE);
            }
        }
        System.out.println("ADM: LoadMenuItemDefaults: loading default menu items from '" + DefaultsFullPath + "'");
    }

    public static Boolean ImportMenuItems(String ImportPath){

        if (ImportPath==null){
            System.out.println("ADM: ImportMenuItems: null ImportPath passed.");
            return false;
        }
        
        Properties MenuItemProps = new Properties();
        
        //read the properties from the properties file
        try {
            FileInputStream in = new FileInputStream(ImportPath);
            try {
                MenuItemProps.load(in);
                in.close();
            } catch (IOException ex) {
                System.out.println("ADM: ImportMenuItems: IO exception inporting menus " + util.class.getName() + ex);
                return false;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ADM: ImportMenuItems: file not found inporting menus " + util.class.getName() + ex);
            return false;
        }
        
        //backup existing MenuItems before processing the import if any exist
        if (MenuNodeList.size()>0){
            //TODO: add Export back in
            //ExportMenuItems(util.PropertyBackupFile);
        }
        
        if (MenuItemProps.size()>0){
            //clean up existing MenuItems from the SageTV properties file before writing the new ones
            sagex.api.Configuration.RemovePropertyAndChildren(util.SagePropertyLocation);
            
            //load MenuItems from the properties file and write to the Sage properties
            for (String tPropertyKey : MenuItemProps.stringPropertyNames()){
                sagex.api.Configuration.SetProperty(tPropertyKey, MenuItemProps.getProperty(tPropertyKey));
                
                //System.out.println("ADM: ImportMenuItems: imported - '" + tPropertyKey + "' = '" + MenuItemProps.getProperty(tPropertyKey) + "'");
            }
            
            //now load the properties from the Sage properties file
            LoadMenuItemsFromSage();

        }
        System.out.println("ADM: ImportMenuItems: completed for '" + ImportPath + "'");
        return true;
    }
 
    public static void ExportMenuItems(String ExportFile){
        String PropLocation = "";
        String ExportFilePath = util.ADMLocation + File.separator + ExportFile;
        //System.out.println("ADM: ExportMenuItems: Full Path = '" + ExportFilePath + "'");
        
        //iterate through all the MenuItems and save to a Property Collection
        Properties MenuItemProps = new Properties();

        for (String tName : MenuNodeList.keySet()){
            PropLocation = util.SagePropertyLocation + tName;
            if (GetMenuItemAction(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/Action",GetMenuItemAction(tName));
            }
            if (GetMenuItemActionType(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/ActionType", GetMenuItemActionType(tName));
            }
            if (GetMenuItemBGImageFile(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/BGImageFile", GetMenuItemBGImageFile(tName));
            }
            if (GetMenuItemButtonText(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/ButtonText", GetMenuItemButtonText(tName));
            }
            MenuItemProps.setProperty(PropLocation + "/Name", tName);
            if (GetMenuItemParent(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/Parent", GetMenuItemParent(tName));
            }
            if (GetMenuItemSortKey(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/SortKey", GetMenuItemSortKey(tName).toString());
            }
            if (GetMenuItemSubMenu(tName)!=null){
                MenuItemProps.setProperty(PropLocation + "/SubMenu", GetMenuItemSubMenu(tName));
            }
            MenuItemProps.setProperty(PropLocation + "/HasSubMenu", GetMenuItemHasSubMenu(tName).toString());
            MenuItemProps.setProperty(PropLocation + "/IsDefault", GetMenuItemIsDefault(tName).toString());
            MenuItemProps.setProperty(PropLocation + "/IsActive", GetMenuItemIsActive(tName).toString());
            //System.out.println("ADM: ExportMenuItems: exported - '" + entry.getValue().getName() + "'");
            
        }
        //if the export file exists then delete it before exporting
        
        //write the properties to the properties file
        try {
            FileOutputStream out = new FileOutputStream(ExportFilePath);
            try {
                MenuItemProps.store(out, util.PropertyComment);
                out.close();
            } catch (IOException ex) {
                System.out.println("ADM: ExportMenuItems: error exporting menus " + util.class.getName() + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ADM: ExportMenuItems: error exporting menus " + util.class.getName() + ex);
        }

        System.out.println("ADM: ExportMenuItems: exported " + MenuNodeList.size() + " MenuItems");
        
        return;
    }
    
    
    private static void AddNode(MenuNode aNode){
        if (!NodeExists(root, aNode.Name)){
            //check if the current nodes parent exists yet
            if (aNode.Parent.equals(util.TopMenu)){
                //root.add(new DefaultMutableTreeNode(aNode));
                InsertNode(root, aNode);
                System.out.println("ADM: AddNode: node '" + aNode.ButtonText + "' not found so adding to ROOT");
            }else{
                AddNode(MenuNodeList.get(aNode.Parent));
                DefaultMutableTreeNode tParent = FindNode(root, aNode.Parent);
                //tParent.add(new DefaultMutableTreeNode(aNode));
                InsertNode(tParent, aNode);
                System.out.println("ADM: AddNode: node '" + aNode.ButtonText + "' not found so adding");
            }
        }else{
            System.out.println("ADM: AddNode: node '" + aNode.ButtonText + "' already exists");
        }
    }
    
    private static void InsertNode(DefaultMutableTreeNode iParent, MenuNode iNode){
        //insert the node according to the SortKey value
        if ( iParent.getChildCount() != 0 ) {

            DefaultMutableTreeNode tlastChild = (DefaultMutableTreeNode)iParent.getFirstChild() ;
            MenuNode lastChild = (MenuNode)tlastChild.getUserObject() ;
            //MenuNode newChildA = (MenuNode) iNode ;
            if ( iNode.SortKey < lastChild.SortKey ) {
                // Its at the top of the list
                InsertNode(iParent,iNode,0);
            }
            else if ( iParent.getChildCount() == 1 ) {
                // There is only one element and since it ain't less than then well put it after
                InsertNode(iParent,iNode,1);
            } else { 
                // we gotta go look for the right spot to insert it
                Boolean done = Boolean.FALSE ;
                for ( int i = 1 ; i < iParent.getChildCount() && !done ; i++ ) {

                    DefaultMutableTreeNode tnextChild = (DefaultMutableTreeNode)iParent.getChildAt(i) ;
                    MenuNode nextChild = (MenuNode)tnextChild.getUserObject() ;
                    if (( iNode.SortKey > lastChild.SortKey ) && ( iNode.SortKey < nextChild.SortKey ) ){
                        // Ok it needs to go between these two
                        InsertNode(iParent,iNode,i);
                        done = Boolean.TRUE;
                    }
                }
                if ( !done ) { // didn't find a place to insert the node must be the last one
                    InsertNode(iParent,iNode,iParent.getChildCount());
                }
            }            
        }else{
            //no children so just do an add
            InsertNode(iParent,iNode,-1);
        }
    }
    
    private static void InsertNode(DefaultMutableTreeNode iParent, MenuNode iNode, Integer iLocation){
        DefaultMutableTreeNode tNode = new DefaultMutableTreeNode(iNode);
        iNode.NodeItem = tNode;
        if (iLocation>=0){
            //do an insert
            iParent.insert(tNode, iLocation);
        }else{
            //do an add for -1
            iParent.add(tNode);
        }
    }
    
    public static void Test(){
        LoadMenuItemsFromSage();
        System.out.println("ADM: TEST NODES: ");
        ListChildren(root);
        
        //find by Node
//        FindNode(root, Testing) ;
//        DefaultMutableTreeNode Child3 = FindNode(root, "Child1 - Child3") ;
//
//        //move Child 3 up one
//        moveNode(Child3,-1);
//        ListChildren(root);
//        moveNode(Child3,-1);
//        ListChildren(root);
//        moveNode(Child3,-1);
//        ListChildren(root);
//
//        setParent(Child3, FindNode(root, "Child2"));
//        ListChildren(root);
    }

    @SuppressWarnings("unchecked")
    public static void ListChildren(DefaultMutableTreeNode Node){
        Enumeration<DefaultMutableTreeNode> en = Node.preorderEnumeration();
        String tButtonText;
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            tButtonText = tMenu.ButtonText;
            System.out.println("ADM: TEST NODES: Child = '" + child + "' SortKey = '" + tMenu.SortKey + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "' HasChildren = '" + GetMenuItemHasChildren(tMenu.Name) + "' buttonText = '" + tButtonText + "' Path = '" + GetPath(child) + "'"  );
        }         
    }
    
    @SuppressWarnings("unchecked")
    public static DefaultMutableTreeNode FindNode(DefaultMutableTreeNode Root, DefaultMutableTreeNode Node){
        Enumeration<DefaultMutableTreeNode> en = Root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            if(child.equals(Node)) 
            { 
                //tree node with string found 
                //System.out.println("ADM: FindNode: '" + Node + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return child;                          
            } 
        }         
        //System.out.println("ADM: FindNode: '" + Node + "' not found.");
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static DefaultMutableTreeNode FindNode(DefaultMutableTreeNode Root, String NodeKey){
        Enumeration<DefaultMutableTreeNode> en = Root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            if(NodeKey.equals(child.getUserObject().toString())) 
            { 
                //tree node with string found 
                //System.out.println("ADM: FindNode: '" + NodeKey + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return child;                          
            } 
        }         
        //System.out.println("ADM: FindNode: '" + NodeKey + "' not found.");
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static Boolean NodeExists(DefaultMutableTreeNode Root, String NodeKey){
        Enumeration<DefaultMutableTreeNode> en = Root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            if(NodeKey.equals(child.getUserObject().toString())) 
            { 
                //tree node with string found 
                //System.out.println("ADM: NodeExists: '" + NodeKey + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return Boolean.TRUE;                          
            } 
        }         
        //System.out.println("ADM: NodeExists: '" + NodeKey + "' not found.");
        return Boolean.FALSE;
    }
    
    public static String GetPath(DefaultMutableTreeNode Node){
        String OutPath = null;
        TreeNode[] path = Node.getPath();
        for (TreeNode pathnode : path){
            DefaultMutableTreeNode pathnodea = (DefaultMutableTreeNode)pathnode;
            MenuNode tMenu = (MenuNode)pathnodea.getUserObject();
            if (!tMenu.Name.equals(util.TopMenu)){
                if (OutPath == null){
                    OutPath = tMenu.ButtonText;
                }else{
                    OutPath = OutPath + " / " + tMenu.ButtonText;
                }
            }
        }
        return OutPath;
    }
    
    public static void setParent( DefaultMutableTreeNode Node, DefaultMutableTreeNode newParent ){
        if ( null == Node ) return;                         // No node selected   
        if ( null == newParent ) return;                    // No Parent provided    
        DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode)Node.getParent();   
        oldParent.remove(Node);   
        newParent.add(Node);  
        System.out.println("ADM: setParent: node = '" + Node + "' oldParent = '" + oldParent +"' newParent = '" + newParent + "'");
    }
    
    public static void moveNode( DefaultMutableTreeNode Node, int aDelta ){
        if ( null == Node ) return;                         // No node selected   
        DefaultMutableTreeNode lParent = (DefaultMutableTreeNode)Node.getParent();   
        if ( null == lParent ) return;                      // Cannot move the Root!   
        int lOldIndex = lParent.getIndex( Node );   
        int lNewIndex = lOldIndex + aDelta;   
        if ( lNewIndex < 0 ) return;                        // Cannot move first child up   
        if ( lNewIndex >= lParent.getChildCount() ) return; // Cannot move last child down   
        lParent.remove(Node);   
        lParent.insert( Node, lNewIndex );  
        //TODO: need to save the indexes of all this parents children
    }  
    
    private static void Save(String Name, String PropType, String Setting){
        String PropLocation = util.SagePropertyLocation + Name;
        sagex.api.Configuration.SetProperty(PropLocation + "/" + PropType, Setting);
        //no save the specifc node field change
        if (PropType.equals("Action")){
            MenuNodeList.get(Name).ActionAttribute = Setting;
        }else if (PropType.equals("ActionType")){
            MenuNodeList.get(Name).ActionType = Setting;
        }else if (PropType.equals("BGImageFile")){
            MenuNodeList.get(Name).BGImageFile = Setting;
        }else if (PropType.equals("ButtonText")){
            MenuNodeList.get(Name).ButtonText = Setting;
        }else if (PropType.equals("HasSubMenu")){
            MenuNodeList.get(Name).HasSubMenu = Boolean.parseBoolean(Setting);
        }else if (PropType.equals("IsActive")){
            MenuNodeList.get(Name).IsActive = Boolean.parseBoolean(Setting);
        }else if (PropType.equals("IsDefault")){
            MenuNodeList.get(Name).IsDefault = Boolean.parseBoolean(Setting);
        }else if (PropType.equals("SubMenu")){
            MenuNodeList.get(Name).SubMenu = Setting;
//        }else if (PropType.equals("")){
//            MenuNodeList.get(Name). = Setting;
//        }else if (PropType.equals("")){
//            MenuNodeList.get(Name). = Setting;
//        }else if (PropType.equals("")){
//            MenuNodeList.get(Name). = Setting;
            
        }
        
        
        System.out.println("ADM: Save completed for '" + PropType + "' '" + Name + "' = '" + Setting + "'");
    }
    
    public static String GetNewMenuItemName(){
        Boolean UniqueName = Boolean.FALSE;
        String NewName = null;
        while (!UniqueName){
            NewName = GenerateRandomadmName();
            //check to see that the name is unique from other existing MenuItemNames
            UniqueName = !MenuNodeList.containsKey(NewName);
        }
        return NewName;
    }

    private static String GenerateRandomadmName(){
        char[] buf = new char[10];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = util.symbols[random.nextInt(util.symbols.length)];
        return "adm" + new String(buf);
    }


}