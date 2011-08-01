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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import sagex.UIContext;

/**
 *
 * @author jusjoken
 */
public class MenuNode {
    public static DefaultMutableTreeNode Testing;

    private String Parent = "";
    public String Name = "";
    private String ButtonText = "";
    private String SubMenu = "";
    private String ActionAttribute = "";
    private String ActionType = "";
    private String BGImageFile = "";
    private String BGImageFilePath = "";
    private Boolean IsDefault = false;
    private util.TriState IsActive = util.TriState.YES;
    private List<String> BlockedSageUsersList = new LinkedList<String>();
    private Integer SortKey = 0;
    private DefaultMutableTreeNode NodeItem;
    private Action.ExternalAction ActionExternal = null;
    public static Integer SortKeyCounter = 0;
    public static Map<String,MenuNode> InternalMenuNodeList = new LinkedHashMap<String,MenuNode>();
    public static Map<String,LinkedHashMap> UIMenuNodeList = new LinkedHashMap<String,LinkedHashMap>();
    public static Map<String,DefaultMutableTreeNode> UIroot = new LinkedHashMap<String,DefaultMutableTreeNode>();
    public static DefaultMutableTreeNode Internalroot = new DefaultMutableTreeNode(util.OptionNotFound);
    public static final String SageUserAdministrator = "Administrator";

    public MenuNode(String bName){
        //create a MenuItem with just default values
        this(util.TopMenu,bName,0,util.ButtonTextDefault,null,util.ActionTypeDefault,null,null,Boolean.FALSE,util.TriState.YES);
    }
    
    public MenuNode(String bParent, String bName, Integer bSortKey, String bButtonText, String bSubMenu, String bActionType, String bAction, String bBGImageFile, Boolean bIsDefault, util.TriState bIsActive){
        Parent = bParent;
        Name = bName;
        ButtonText = bButtonText;
        SubMenu = bSubMenu;
        ActionType = bActionType;
        ActionAttribute = bAction;
        SetBGImageFileandPath(bBGImageFile);
        IsDefault = bIsDefault;
        IsActive = bIsActive;
        SortKey = bSortKey;
        ActionExternal = new Action.ExternalAction(Name);
        MenuNodeList().put(Name, this);
    }
    
    @Override
    public String toString(){
        //TODO: may want to change this to ButtonText
        return Name;
        //return ButtonText;
    }

    public static String GetMenuItemAction(String Name){
        //System.out.println("ADM: mGetMenuItemAction for '" + Name + "' = '" + MenuNodeList().get(Name).ActionAttribute + "'");
        try {
            if (MenuNodeList().get(Name).ActionType.equals(Action.LaunchExternalApplication)){
                return MenuNodeList().get(Name).ActionExternal.GetApplication();
            }else{
                return MenuNodeList().get(Name).ActionAttribute;
            }
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemAction(String Name, String Setting){
        Save(Name, "Action", Setting);
    }

    public static String GetMenuItemActionType(String Name){
        try {
            return MenuNodeList().get(Name).ActionType;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemActionType(String Name, String Setting){
        Save(Name, "ActionType", Setting);
    }

    public static Action.ExternalAction GetMenuItemActionExternal(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    public static String GetMenuItemActionExternalApplication(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal.GetApplication();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    public static String GetMenuItemActionExternalArguments(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal.GetArguments();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    public static String GetMenuItemActionExternalWindowType(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal.GetWindowType();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    public static String GetMenuItemActionExternalWaitForExit(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal.GetWaitForExit();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    public static String GetMenuItemActionExternalSageStatus(String Name){
        try {
            return MenuNodeList().get(Name).ActionExternal.GetSageStatus();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemActionExternalApplication(String Name, String Setting){
        //change the setting to the next +1, previous -1, or dont change it and then save
        MenuNodeList().get(Name).ActionExternal.SetApplication(Setting);
    }
    public static void SetMenuItemActionExternalArguments(String Name, String Setting){
        //change the setting to the next +1, previous -1, or dont change it and then save
        MenuNodeList().get(Name).ActionExternal.SetArguments(Setting);
    }
    public static void SetMenuItemActionExternalWindowType(String Name, Integer Delta){
        //change the setting to the next +1, previous -1, or dont change it and then save
        MenuNodeList().get(Name).ActionExternal.ChangeWindowType(Delta);
    }
    public static void SetMenuItemActionExternalWaitForExit(String Name){
        //change the setting to the next +1, previous -1, or dont change it and then save
        MenuNodeList().get(Name).ActionExternal.ChangeWaitForExit();
    }
    public static void SetMenuItemActionExternalSageStatus(String Name, Integer Delta){
        //change the setting to the next +1, previous -1, or dont change it and then save
        MenuNodeList().get(Name).ActionExternal.ChangeSageStatus(Delta);
    }
    
    private void SetBGImageFileandPath(String bBGImageFile){
        //see if using a GlobalVariable from a Theme or a path to an image file
        BGImageFile = bBGImageFile;
        BGImageFilePath = util.GetSageBGFile(bBGImageFile);
        //System.out.println("ADM: mSetBGImageFileandPath for '" + Name + "' BGImageFile = '" + BGImageFile + "' BGImageFilePath = '" + BGImageFilePath + "'");
    }
    
    public static String GetMenuItemBGImageFileButtonText(String Name){
        return util.GetSageBGButtonText(MenuNodeList().get(Name).BGImageFile);
    }
    
    public static String GetMenuItemBGImageFile(String Name){
        if(MenuNodeList().get(Name).BGImageFile==null){
            return util.ListNone;
        }else{
            try {
                return MenuNodeList().get(Name).BGImageFile;
            } catch (Exception e) {
                System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
                return null;
            }
        }
    }

    public static String GetMenuItemBGImageFilePath(String Name){
        //System.out.println("ADM: mGetMenuItemBGImageFilePath for '" + Name + "' returning '" + MenuNodeList().get(Name).BGImageFilePath + "'");
        try {
            if (MenuNodeList().get(Name).BGImageFilePath==null){
                if (MenuNodeList().get(Name).Parent.equals(util.TopMenu)){
                    return null;
                }else{
                    return GetMenuItemBGImageFilePath(MenuNodeList().get(Name).Parent);
                }
            }else{
                return MenuNodeList().get(Name).BGImageFilePath;
            }
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemBGImageFile(String Name, String Setting){
        if (Setting.equals(util.ListNone) || Setting==null){
            Save(Name, "BGImageFile", util.ListNone);
        }else{
            Save(Name, "BGImageFile", Setting);
        }
        MenuNodeList().get(Name).SetBGImageFileandPath(Setting);
    }

    public static String GetMenuItemButtonText(String Name){
        //System.out.println("ADM: mGetMenuItemButtonText: Name '" + Name + "' NodeListCount = '" + (MenuNode) UIMenuNodeList.get("SAGETV_PROCESS_LOCAL_UI").get(Name) + "' root = '" + UIroot.get("SAGETV_PROCESS_LOCAL_UI").getChildCount() + "'");
        if (Name.equals(util.TopMenu)){
            return "Top Level";
        }else{
            try {
                return MenuNodeList().get(Name).ButtonText;
            } catch (Exception e) {
                System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
                return null;
            }
        }
    }

    public static String GetMenuItemButtonTextNewTest(String Name){
        if (MenuNodeList().get(Name).ButtonText.equals(util.ButtonTextDefault)){
            return "";
        }else{
            return GetMenuItemButtonText(Name);
        }
    }

    public static void SetMenuItemButtonText(String Name, String Setting){
        Save(Name, "ButtonText", Setting);
    }

    public static Boolean GetMenuItemHasSubMenu(String Name){
        try {
            return !MenuNodeList().get(Name).NodeItem.isLeaf();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static util.TriState GetMenuItemIsActive(String Name){
        try {
            return MenuNodeList().get(Name).IsActive;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static String GetMenuItemIsActiveIncludingParentFormatted(String Name){
        try {
            if (MenuNodeList().get(Name).IsActive.equals(util.TriState.NO)){
                return "No";
            }else if (MenuNodeList().get(Name).IsActive.equals(util.TriState.OTHER)){
                return "User Based";
            }else if (GetMenuItemIsActiveIncludingParent(Name).equals(util.TriState.YES)){
                return "Yes";
            }else if (GetMenuItemIsActiveIncludingParent(Name).equals(util.TriState.OTHER)){
                return "Yes (Parent: User Based)";
            }
            // in this case this item is active BUT the parent is not
            return "Yes (Parent: No)";
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    
    public static util.TriState GetMenuItemIsActiveIncludingParent(String Name){
        try {
            if (MenuNodeList().get(Name).IsActive.equals(util.TriState.NO)){
                return util.TriState.NO;
            }else if (MenuNodeList().get(Name).IsActive.equals(util.TriState.OTHER)){
                return util.TriState.OTHER;
            }
            TreeNode[] path = MenuNodeList().get(Name).NodeItem.getPath();
            for (TreeNode pathnode : path){
                DefaultMutableTreeNode pathnodea = (DefaultMutableTreeNode)pathnode;
                MenuNode tMenu = (MenuNode)pathnodea.getUserObject();
                //System.out.println("ADM: mGetMenuItemIsActiveIncludingParent for '" + Name + "' for item = '" + tMenu.ButtonText + "'");
                if (tMenu.IsActive.equals(util.TriState.NO)){
                    //System.out.println("ADM: mGetMenuItemIsActiveIncludingParent for '" + Name + "' for item = '" + tMenu.ButtonText + "' = NO");
                    return util.TriState.NO;
                }else if(tMenu.IsActive.equals(util.TriState.OTHER)){
                    //System.out.println("ADM: mGetMenuItemIsActiveIncludingParent for '" + Name + "' for item = '" + tMenu.ButtonText + "' = OTHER");
                    return util.TriState.OTHER;
                }
            }
            //System.out.println("ADM: mGetMenuItemIsActiveIncludingParent for '" + Name + "' YES");
            return util.TriState.YES;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemIsActive(String Name, util.TriState Setting){
        Save(Name, "IsActive", Setting.toString());
    }

    public static void ChangeMenuItemIsActive(String Name){
        try {
            if(MenuNodeList().get(Name).IsActive.equals(util.TriState.YES)){
                Save(Name, "IsActive", util.TriState.NO.toString());
            }else if(MenuNodeList().get(Name).IsActive.equals(util.TriState.NO)){
                Save(Name, "IsActive", util.TriState.OTHER.toString());
            }else{
                Save(Name, "IsActive", util.TriState.YES.toString());
            }
        } catch (Exception e) {
        }
    }

    public static String GetMenuItemBlockedSageUsersList(String Name){
        try {
            return util.ConvertListtoString(MenuNodeList().get(Name).BlockedSageUsersList);
        } catch (Exception e) {
            return "";
        }
    }
    
    public static List<String> GetSageUsersList(){
        //return a list of SageUsers in sorted order with the Administrator removed
        List<String> ProfileList = new LinkedList<String>();
        ProfileList.addAll(Arrays.asList(sagex.api.Security.GetSecurityProfiles(new UIContext(sagex.api.Global.GetUIContextName()))));
        ProfileList.remove(SageUserAdministrator);
        Collections.sort(ProfileList);
        return ProfileList;
    }
    
    public static Integer GetSageUsersListCount(){
        //return a count of SageUsers with the Administrator removed
        return sagex.api.Security.GetSecurityProfiles(new UIContext(sagex.api.Global.GetUIContextName())).length - 1;
    }
    
    public static Integer GetSageUsersBlockedListCount(String Name){
        Integer tInt = 0;
        for (String Item : sagex.api.Security.GetSecurityProfiles(new UIContext(sagex.api.Global.GetUIContextName()))){
            try {
                if (MenuNodeList().get(Name).BlockedSageUsersList.contains(Item)){
                    tInt++;
                }
            } catch (Exception e) {
                //do nothing
            }
        }
        return tInt;
    }
    
    //use a Tokenized String to list all the users that are NOT allowed to use this menu item
    //if a user is not in the String List then it is assumed they are ALLOWED to use the menu item
    //therefore - new Users created in Sage will have access to all menu items until removed specifically
    public static Boolean IsSageUserAllowed(String Name, String SageUser){
        try {
            if (SageUser.equals(SageUserAdministrator)){
                return Boolean.TRUE;
            }else{
                //if the SageUser is NOT in the list then the user is ALLOWED
                return !MenuNodeList().get(Name).BlockedSageUsersList.contains(SageUser);
            }
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }
    
    public static void ChangeSageUserAllowed(String Name, String SageUser){
        if (!SageUser.equals(SageUserAdministrator)){
            try {
                //if the user is in the blocked list then remove it from the list otherwise add it
                if (MenuNodeList().get(Name).BlockedSageUsersList.contains(SageUser)){
                    MenuNodeList().get(Name).BlockedSageUsersList.remove(SageUser);
                }else{
                    MenuNodeList().get(Name).BlockedSageUsersList.add(SageUser);
                }
                Save(Name, "BlockedSageUsersList", util.ConvertListtoString(MenuNodeList().get(Name).BlockedSageUsersList));
            } catch (Exception e) {
                System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            }
        }
    }
    
    public static Boolean GetMenuItemIsDefault(String Name){
        try {
            return MenuNodeList().get(Name).IsDefault;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemIsDefault(String Name, Boolean Setting){
        //System.out.println("ADM: mSetMenuItemIsDefault: Name '" + Name + "' Setting '" + Setting + "'");
        if (Setting==Boolean.TRUE){
            //System.out.println("ADM: mSetMenuItemIsDefault: true Name '" + Name + "' Setting '" + Setting + "'");
            //first clear existing Default settings for Menu Items with the same parent 
            ClearSubMenuDefaults(MenuNodeList().get(Name).Parent);
            Save(Name, "IsDefault", Setting.toString());
        }else{
            //System.out.println("ADM: mSetMenuItemIsDefault: false Name '" + Name + "' Setting '" + Setting + "'");
            Save(Name, "IsDefault", Setting.toString());
            //ensure at least 1 item remaining is a default
            ValidateSubMenuDefault(MenuNodeList().get(Name).Parent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void ValidateSubMenuDefault(String bParent){
        //ensure that 1 and only 1 item is set as the default
        Boolean FoundDefault = Boolean.FALSE;
        
        if (MenuNodeList().get(bParent).NodeItem.getChildCount()>0){

            Enumeration<DefaultMutableTreeNode> en = MenuNodeList().get(bParent).NodeItem.children();
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
                DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)MenuNodeList().get(bParent).NodeItem.getFirstChild();
                MenuNode tMenu = (MenuNode)firstChild.getUserObject();
                System.out.println("ADM: mValidateSubMenuDefault for '" + bParent + "' : no Default found so setting first = '" + tMenu.Name + "'");
                Save(tMenu.Name, "IsDefault", Boolean.TRUE.toString());
            }else {
                System.out.println("ADM: mValidateSubMenuDefault for '" + bParent + "' : Default already set");
            }
        }else{
            //no subMenu items so make sure this parent's SubMenu settings are correct
            SetMenuItemSubMenu(bParent, util.ListNone);
            System.out.println("ADM: mValidateSubMenuDefault for '" + bParent + "' : no SubMenu items found");
        }
    }
        
    //clear all defaults for a submenu - used prior to setting a new default to ensure there is not more than one
    @SuppressWarnings("unchecked")
    public static void ClearSubMenuDefaults(String bParent){
        if (MenuNodeList().get(bParent).NodeItem.getChildCount()>0){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList().get(bParent).NodeItem.children();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsDefault){
                    //Save setting
                    Save(tMenu.Name, "IsDefault", Boolean.FALSE.toString());
                }
            }         
        }
        System.out.println("ADM: mClearSubMenuDefaults for '" + bParent + "' '" + MenuNodeList().get(bParent).NodeItem.getChildCount() + "' cleared");
    }
    
    @SuppressWarnings("unchecked")
    public static String GetSubMenuDefault(String bParent){
        if (MenuNodeList().get(bParent).NodeItem.getChildCount()>0){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList().get(bParent).NodeItem.children();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (tMenu.IsDefault){
                    System.out.println("ADM: mGetSubMenuDefault for '" + bParent + "' Default = '" + tMenu.Name + "'");
                    return tMenu.Name;
                }
            }         
        }
        System.out.println("ADM: mGetSubMenuDefault for '" + bParent + "' - none found");
        return "";
    }

    public static Integer GetMenuItemLevel(String Name){
        try {
            return MenuNodeList().get(Name).NodeItem.getLevel();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemName(String Name){
        Save(Name, "Name", Name);
    }

    public static String GetMenuItemParent(String Name){
        //get the parent from the Tree structure
        if (Name.equals(util.TopMenu)){
            //System.out.println("ADM: mGetMenuItemParent for '" + Name + "' returning null");
            return null;
        }else{
            //System.out.println("ADM: mGetMenuItemParent for '" + Name + "' = '" + MenuNodeList().get(Name).NodeItem.getParent().toString() + "'");
            try {
                return MenuNodeList().get(Name).NodeItem.getParent().toString();
            } catch (Exception e) {
                System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
                return null;
            }
        }
    }

    //moves the MenuNode to another parent if valid
    public static void SetMenuItemParent(String Name, String NewParent){
        //make sure the parent is not the MenuItem itself
        if(Name.equals(NewParent) || NewParent.equals(MenuNodeList().get(Name).Parent) || Name.equals(util.TopMenu)){
            //do nothing as changing the parent here is invalid
        }else{
            String OldParent = MenuNodeList().get(Name).NodeItem.getParent().toString();
            
            MenuNodeList().get(OldParent).NodeItem.remove(MenuNodeList().get(Name).NodeItem);
            MenuNodeList().get(NewParent).NodeItem.add(MenuNodeList().get(Name).NodeItem);
            Save(Name, "Parent", NewParent);

            //update the sort keys for the old and new parents
            SortKeyUpdate(MenuNodeList().get(OldParent).NodeItem);
            SortKeyUpdate(MenuNodeList().get(NewParent).NodeItem);

            //check the new parent and set it's SubMenu properly
            if (!NewParent.equals(util.TopMenu)){
                SetMenuItemSubMenu(NewParent,util.ListNone);
            }
            
            //make sure the old and new SubMenus have a single default item
            ValidateSubMenuDefault(OldParent);
            ValidateSubMenuDefault(NewParent);
            System.out.println("ADM: mSetMenuItemParent: Parent changed for '" + Name + "' to = '" + NewParent + "'");
        }
    }
    
    public static Integer GetMenuItemSortKey(String Name){
        try {
            return MenuNodeList().get(Name).SortKey;
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public void setSortKey(String SortKey) {
        Integer tSortKey = 0;
        try {
            tSortKey = Integer.valueOf(SortKey);
        } catch (NumberFormatException ex) {
            System.out.println("ADM: msetSortKey: error converting '" + SortKey + "' " + util.class.getName() + ex);
            tSortKey = SortKeyCounter++;
        }
        this.SortKey = tSortKey;
    }
    
    public static void ChangeSortOrder(String Name, Integer aDelta){
        if (moveNode(MenuNodeList().get(Name).NodeItem, aDelta)){
            SortKeyUpdate((DefaultMutableTreeNode)MenuNodeList().get(Name).NodeItem.getParent());
            System.out.println("ADM: mChangeSortOrder: moving '" + Name + "' by '" + aDelta.toString() + "'");
        }else{
            System.out.println("ADM: mChangeSortOrder: NOT ABLE to move '" + Name + "' by '" + aDelta.toString() + "'");
        }
    }
    
    public static Boolean moveNode( DefaultMutableTreeNode Node, int aDelta ){
        if ( null == Node ) return Boolean.FALSE;                         // No node selected   
        DefaultMutableTreeNode lParent = (DefaultMutableTreeNode)Node.getParent();   
        if ( null == lParent ) return Boolean.FALSE;                      // Cannot move the Root!   
        int lOldIndex = lParent.getIndex( Node );   
        int lNewIndex = lOldIndex + aDelta;   
        if ( lNewIndex < 0 ) return Boolean.FALSE;                        // Cannot move first child up   
        if ( lNewIndex >= lParent.getChildCount() ) return Boolean.FALSE; // Cannot move last child down   
        lParent.remove(Node);   
        lParent.insert( Node, lNewIndex );  
        return  Boolean.TRUE;
    }  
    
    public static void SortKeyUpdate(){
        SortKeyUpdate(root());
    }
    
    @SuppressWarnings("unchecked")
    public static void SortKeyUpdate(DefaultMutableTreeNode aParent){
        //update all the sortkey values to the index starting from the aParent
        Enumeration<DefaultMutableTreeNode> en;
        if (aParent.equals(root())){
            en = aParent.preorderEnumeration();
        }else{
            en = aParent.children();
        }
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (!tMenu.Name.equals(util.TopMenu)){
                tMenu.SortKey = child.getParent().getIndex(child);
                String PropLocation = util.SagePropertyLocation + tMenu.Name + "/" + "SortKey";
                util.SetProperty(PropLocation, tMenu.SortKey.toString());
                //System.out.println("ADM: mSortKeyUpdate: Child = '" + child + "' SortKey = '" + tMenu.SortKey + "' Parent = '" + child.getParent() + "'"  );
            }
        }         
        System.out.println("ADM: mSortKeyUpdate: completed for Parent = '" + aParent + "'"  );
    }
    
    //the SubMenu field is only filled in if using a built in Sage SubMenu
    // otherwise, the Name of the MenuItem is returned if the MenuItem has a SubMenu
    public String getSubMenu() {
        if (SubMenu==null){
            if (!NodeItem.isLeaf()){
                return Name;
            }else{
                return SubMenu;
            }
        }else{
            return SubMenu;
        }
    }

    public static String GetMenuItemSubMenu(String Name){
        try {
            return MenuNodeList().get(Name).getSubMenu();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static String GetMenuItemSubMenuButtonText(String Name){
        try {
            return util.GetSubMenuListButtonText(MenuNodeList().get(Name).getSubMenu(), GetMenuItemLevel(Name));
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + Name + "' Exception = '" + e + "'");
            return null;
        }
    }

    public static void SetMenuItemSubMenu(String Name, String Setting){
        //System.out.println("ADM: mSetMenuItemSubMenu for '" + Name + "' Setting = '" + Setting + "'");
        if (Setting.equals(util.ListNone) || Setting==null){
            //set the SubMenu field
            Save(Name, "SubMenu", null);
        }else{
            //set the SubMenu field
            Save(Name, "SubMenu", Setting);
        }
    }

    public static Boolean IsSubMenuItem(String bParent, String Item){
        //check if Item is a child of bParent
        if (bParent==null || Item==null){
            return Boolean.FALSE;
        }
        try {
            if (MenuNodeList().get(Item).NodeItem.getParent().toString().equals(bParent)){
                System.out.println("ADM: mIsSubMenuItem for Parent = '" + bParent + "' Item '" + Item + "' found");
                return Boolean.TRUE;
            }else{
                System.out.println("ADM: mIsSubMenuItem for Parent = '" + bParent + "' Item '" + Item + "' NOT found");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available for '" + bParent + "' Exception = '" + e + "'");
            return null;
        }
    }

    //returns the full list of ALL menu items regardless of parent
    public static Collection<String> GetMenuItemNameList(){
        try {
            return MenuNodeList().keySet();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available. Exception = '" + e + "'");
            return null;
        }
    }
    
    //returns only menu items for a specific parent that are active
    public static Collection<String> GetMenuItemNameList(String Parent){
        return GetMenuItemNameList(Parent, Boolean.FALSE);
    }

    //returns menu items for a specific parent
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemNameList(String Parent, Boolean IncludeInactive){
        Collection<String> bNames = new LinkedHashSet<String>();
        String tActiveUser = sagex.api.Security.GetActiveSecurityProfile(new UIContext(sagex.api.Global.GetUIContextName()));
        if (MenuNodeList().containsKey(Parent) && MenuNodeList().get(Parent).NodeItem!=null){
            Enumeration<DefaultMutableTreeNode> en = MenuNodeList().get(Parent).NodeItem.children();
            while (en.hasMoreElements())   {
                DefaultMutableTreeNode child = en.nextElement();
                MenuNode tMenu = (MenuNode)child.getUserObject();
                if (IncludeInactive==true){
                    bNames.add(tMenu.Name);
                }else if(tMenu.IsActive.equals(util.TriState.YES)){
                    bNames.add(tMenu.Name);
                }else if(tMenu.IsActive.equals(util.TriState.OTHER)){
                    //only add if the active user is allowed to see this Menu Item
                    if (!tMenu.BlockedSageUsersList.contains(tActiveUser)){
                        bNames.add(tMenu.Name);
                    }
                }
                //otherwise do not add the Menu Item
            }         
        }
        System.out.println("ADM: mGetMenuItemNameList for '" + Parent + "' : IncludeInactive = '" + IncludeInactive.toString() + "' " + bNames);
        return bNames;
    }
    
    //returns the count of ALL MenuNode ITems
    public static int GetMenuItemCount(){
        try {
            return MenuNodeList().size();
        } catch (Exception e) {
            System.out.println("ADM: mGet... ERROR: Value not available. Exception = '" + e + "'");
            return 0;
        }
    }
    
    //Get the count of MenuItems for a parent that are active
    public static int GetMenuItemCount(String Parent){
        Collection<String> bNames = GetMenuItemNameList(Parent);
        System.out.println("ADM: mGetMenuItemCount for '" + Parent + "' :" + bNames.size());
        return bNames.size();
    }

    //returns only menu items for a specific parent that are active
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemSortedList(Boolean Grouped){
        Collection<String> FinalList = new LinkedHashSet<String>();
        
        Enumeration<DefaultMutableTreeNode> en;
        if (Grouped){
            //Menu Items in Level 1, then Level 2 etc
            en = root().breadthFirstEnumeration();
        }else{
            //Menu Items in Tree Order
            en = root().preorderEnumeration();
        }
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            //add all items except the Top Level menu
            if (!tMenu.Name.equals(util.TopMenu)){
                FinalList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: mGetMenuItemSortedList: Grouped = '" + Grouped.toString() + "' :" + FinalList);
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
        Enumeration<DefaultMutableTreeNode> en = root().preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (child.getLevel()<3){
                ValidParentList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: mGetMenuItemParentList: '" + ValidParentList + "'");
        return ValidParentList;
    }
    
    //get valid parent list for only 1 specific level
    @SuppressWarnings("unchecked")
    public static Collection<String> GetMenuItemParentList(Integer SpecificLevel){
        Collection<String> ValidParentList = new LinkedHashSet<String>();
        Enumeration<DefaultMutableTreeNode> en = root().preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (child.getLevel()==SpecificLevel-1){
                ValidParentList.add(tMenu.Name);
            }
        }         
        System.out.println("ADM: mGetMenuItemParentList: for Level = '" + SpecificLevel + "' List = '" + ValidParentList + "'");
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
        if (MenuNodeList().get(Name).IsDefault){
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
            if (Name.equals(util.TopMenu)){
                return MenuNodeList().get(Name).ButtonText;
            }else{
                return GetPath(MenuNodeList().get(Name).NodeItem);
            }
        }else{
            String tPadded = "" + util.repeat(PrefixPadding,MenuNodeList().get(Name).NodeItem.getLevel()-1 );
            return tPadded + MenuNodeList().get(Name).ButtonText;
        }
    }

    public static void Execute(String Name){
        Action.Execute(Name);
    }
    
    public static String GetActionAttributeButtonText(String Name){
        return Action.GetAttributeButtonText(GetMenuItemActionType(Name), GetMenuItemAction(Name));
    }

    //prepare the environment for a new load or a delete
    public static void CleanMenuNodeListandTree(){
        String UIContext = sagex.api.Global.GetUIContextName();
        //create and store the top menu node
        if (UIroot.containsKey(UIContext)){
            System.out.println("ADM: mCleanMenuNodeListandTree: clearing root for '" + UIContext + "'");
            UIroot.get(UIContext).removeAllChildren();
            UIroot.remove(UIContext);
        }

        //clear the existing MenuItems from the list
        if (UIMenuNodeList.containsKey(UIContext)){
            System.out.println("ADM: mCleanMenuNodeListandTree: clearing MenuNodeList for '" + UIContext + "'");
            UIMenuNodeList.remove(UIContext);
        }
        MenuNodeList().clear();
        root().removeAllChildren();
        
    }
    
    public static void LoadMenuItemsFromSage(){
        String PropLocation = "";

        //cleanup the Nodes and the Tree prior to loading
        CleanMenuNodeListandTree();
        
        //find all MenuItem Name entries from the SageTV properties file
        String[] MenuItemNames = sagex.api.Configuration.GetSubpropertiesThatAreBranches(new UIContext(sagex.api.Global.GetUIContextName()),util.SagePropertyLocation);

        
        if (MenuItemNames.length>0){
            
            //load MenuItems
            for (String tMenuItemName : MenuItemNames){
                //make sure you do not load a TopMenu item - it should never be saved but this is just an extra check
                if (!tMenuItemName.equals(util.TopMenu)){
                    PropLocation = util.SagePropertyLocation + tMenuItemName;
                    MenuNode NewMenuItem = new MenuNode(tMenuItemName);
                    NewMenuItem.ActionAttribute = util.GetProperty(PropLocation + "/Action", null);
                    NewMenuItem.ActionType = util.GetProperty(PropLocation + "/ActionType", util.ActionTypeDefault);
                    NewMenuItem.SetBGImageFileandPath(util.GetProperty(PropLocation + "/BGImageFile", null));
                    NewMenuItem.ButtonText = util.GetProperty(PropLocation + "/ButtonText", util.ButtonTextDefault);
                    NewMenuItem.Name = util.GetProperty(PropLocation + "/Name", tMenuItemName);
                    NewMenuItem.Parent = util.GetProperty(PropLocation + "/Parent", "xTopMenu");
                    NewMenuItem.setSortKey(util.GetProperty(PropLocation + "/SortKey", "0"));
                    NewMenuItem.SubMenu = util.GetProperty(PropLocation + "/SubMenu", null);
                    NewMenuItem.IsDefault = Boolean.parseBoolean(util.GetProperty(PropLocation + "/IsDefault", "false"));
                    NewMenuItem.IsActive = util.GetPropertyAsTriState(PropLocation + "/IsActive", util.TriState.YES);
                    NewMenuItem.BlockedSageUsersList = util.GetPropertyAsList(PropLocation + "/BlockedSageUsersList");
                    NewMenuItem.ActionExternal.Load();
                    System.out.println("ADM: mLoadMenuItemsFromSage: loaded - '" + tMenuItemName + "' = '" + NewMenuItem.ButtonText + "'");
                }else{
                    System.out.println("ADM: mLoadMenuItemsFromSage: skipping - '" + tMenuItemName + "' - should not load a TopMenu item");
                }
            }
            if (MenuNodeList().size()>0){
                //create the tree nodes
                for (MenuNode Node : MenuNodeList().values()){
                    //check if the current node exists yet
                    AddNode(Node);
                }
                //now update the sortkeys from the Tree structure
                SortKeyUpdate();
            }
            
        }else{
            //load a default Menu here.  Load a Diamond Menu if Diamond if active
            System.out.println("ADM: mLoadMenuItemsFromSage: no MenuItems found - loading default menu.");
            LoadMenuItemDefaults();
        }
        System.out.println("ADM: mLoadMenuItemsFromSage: loaded " + MenuNodeList().size() + " MenuItems = '" + MenuNodeList() + "'");
        
        return;
    }
    
    //saves all MenuItems to Sage properties
    @SuppressWarnings("unchecked")
    public static void SaveMenuItemsToSage(){
        
        //clean up existing MenuItems from the SageTV properties file before writing the new ones
        util.RemovePropertyAndChildren(util.SagePropertyLocation);
        //clear the MenuNodeList and rebuild it while saving
        MenuNodeList().clear();
        
        //iterate through all the MenuItems and save to SageTV properties
        Enumeration<DefaultMutableTreeNode> en = root().preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            MenuNode tMenu = (MenuNode)child.getUserObject();
            if (!tMenu.Name.equals(util.TopMenu)){
                SaveMenuItemToSage(tMenu);
            }
            //add the item into the MenuNodeList
            MenuNodeList().put(tMenu.Name, tMenu);
        }         
        System.out.println("ADM: mSaveMenuItemsToSage: saved " + MenuNodeList().size() + " MenuItems");
        
        return;
    }
 
    public static void SaveMenuItemToSage(MenuNode tMenu){
        if (!tMenu.Name.equals(util.TopMenu)){
            String PropLocation = "";
            PropLocation = util.SagePropertyLocation + tMenu.Name;
            util.SetProperty(PropLocation + "/Action", tMenu.ActionAttribute);
            util.SetProperty(PropLocation + "/ActionType", tMenu.ActionType);
            util.SetProperty(PropLocation + "/BGImageFile", tMenu.BGImageFile);
            util.SetProperty(PropLocation + "/ButtonText", tMenu.ButtonText);
            util.SetProperty(PropLocation + "/Name", tMenu.Name);
            util.SetProperty(PropLocation + "/Parent", tMenu.Parent);
            util.SetProperty(PropLocation + "/SortKey", tMenu.SortKey.toString());
            util.SetProperty(PropLocation + "/SubMenu", tMenu.SubMenu);
            util.SetProperty(PropLocation + "/IsDefault", tMenu.IsDefault.toString());
            util.SetProperty(PropLocation + "/IsActive", tMenu.IsActive.toString());
            util.SetPropertyAsList(PropLocation + "/BlockedSageUsersList", tMenu.BlockedSageUsersList);
        }
    }
    
    public static void DeleteMenuItem(String Name){
        //store the parent for later cleanup
        String OldParent = GetMenuItemParent(Name);
        //do all the deletes first
        MenuNodeList().get(Name).NodeItem.removeAllChildren();
        MenuNodeList().get(Name).NodeItem.removeFromParent();
        //Make sure there is still one default Menu Item
        ValidateSubMenuDefault(OldParent);
        //rebuild any lists
        SaveMenuItemsToSage();
        System.out.println("ADM: mDeleteMenuItem: deleted '" + Name + "'");
    }
    
    public static void DeleteAllMenuItems(){

        //backup existing MenuItems before deleting
        if (MenuNodeList().size()>0){
            ExportMenuItems(util.PropertyBackupFile);
        }
        //clean up existing MenuItems from the SageTV properties file
        util.RemovePropertyAndChildren(util.SagePropertyLocation);
        //clean the environment
        CleanMenuNodeListandTree();
        //Create 1 new MenuItem at the TopMenu level
        NewMenuItem(util.TopMenu, 1) ;

        System.out.println("ADM: mDeleteAllMenuItems: completed");
    }
    
    public static String NewMenuItem(String Parent, Integer SortKey){
        String tMenuItemName = GetNewMenuItemName();

        //Create a new MenuItem with defaults
        MenuNode NewMenuItem = new MenuNode(Parent,tMenuItemName,SortKey,util.ButtonTextDefault,null,util.ActionTypeDefault,null,null,Boolean.FALSE,util.TriState.YES);
        SaveMenuItemToSage(NewMenuItem);
        //add the Node to the Tree
        InsertNode(MenuNodeList().get(Parent).NodeItem, NewMenuItem, Boolean.TRUE);
        //ensure there is 1 default item
        ValidateSubMenuDefault(Parent);
        System.out.println("ADM: mNewMenuItem: Parent '" + Parent + "' Name '" + tMenuItemName + "' SortKey = '" + SortKey + "'");
        //util.ListObjectMembers(NewMenuItem);
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
            if ("true".equals(util.GetProperty(DiamondVideoMenuCheckProp, "false"))){
                //show the Videos Menu
                SetMenuItemIsActive(DiamondMenuMovies, util.TriState.YES);
                SetMenuItemIsActive(DiamondMenuVideos, util.TriState.NO);
            }else{
                //show the Movies Menu
                SetMenuItemIsActive(DiamondMenuMovies, util.TriState.NO);
                SetMenuItemIsActive(DiamondMenuVideos, util.TriState.YES);
            }
        }
        System.out.println("ADM: mLoadMenuItemDefaults: loading default menu items from '" + DefaultsFullPath + "'");
    }

    public static Boolean ImportMenuItems(String ImportPath){

        if (ImportPath==null){
            System.out.println("ADM: mImportMenuItems: null ImportPath passed.");
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
                System.out.println("ADM: mImportMenuItems: IO exception inporting menus " + util.class.getName() + ex);
                return false;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ADM: mImportMenuItems: file not found inporting menus " + util.class.getName() + ex);
            return false;
        }
        
        //backup existing MenuItems before processing the import if any exist
        if (MenuNodeList().size()>0){
            System.out.println("ADM: mImportMenuItems: called Export");
            ExportMenuItems(util.PropertyBackupFile);
            System.out.println("ADM: mImportMenuItems: Export returned to Import");
        }
        
        if (MenuItemProps.size()>0){
            //clean up existing MenuItems from the SageTV properties file before writing the new ones
            util.RemovePropertyAndChildren(util.SagePropertyLocation);
            
            //load MenuItems from the properties file and write to the Sage properties
            for (String tPropertyKey : MenuItemProps.stringPropertyNames()){
                util.SetProperty(tPropertyKey, MenuItemProps.getProperty(tPropertyKey));
                
                //System.out.println("ADM: mImportMenuItems: imported - '" + tPropertyKey + "' = '" + MenuItemProps.getProperty(tPropertyKey) + "'");
            }
            
            //now load the properties from the Sage properties file
            LoadMenuItemsFromSage();

        }
        System.out.println("ADM: mImportMenuItems: completed for '" + ImportPath + "'");
        return true;
    }
 
    public static void ExportMenuItems(String ExportFile){
        String PropLocation = "";
        String ExportFilePath = util.ADMLocation + File.separator + ExportFile;
        //System.out.println("ADM: mExportMenuItems: Full Path = '" + ExportFilePath + "'");
        
        //iterate through all the MenuItems and save to a Property Collection
        Properties MenuItemProps = new Properties();

        for (String tName : MenuNodeList().keySet()){
            if (!tName.equals(util.TopMenu)){
                PropLocation = util.SagePropertyLocation + tName;
                PropertyAdd(MenuItemProps,PropLocation + "/Action",GetMenuItemAction(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/ActionType", GetMenuItemActionType(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/BGImageFile", GetMenuItemBGImageFile(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/ButtonText", GetMenuItemButtonText(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/Name", tName);
                PropertyAdd(MenuItemProps,PropLocation + "/Parent", GetMenuItemParent(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/SortKey", GetMenuItemSortKey(tName).toString());
                PropertyAdd(MenuItemProps,PropLocation + "/SubMenu", GetMenuItemSubMenu(tName));
                PropertyAdd(MenuItemProps,PropLocation + "/IsDefault", GetMenuItemIsDefault(tName).toString());
                PropertyAdd(MenuItemProps,PropLocation + "/IsActive", GetMenuItemIsActive(tName).toString());
                PropertyAdd(MenuItemProps,PropLocation + "/BlockedSageUsersList", GetMenuItemBlockedSageUsersList(tName));
                GetMenuItemActionExternal(tName).AddProperties(MenuItemProps);
                //System.out.println("ADM: mExportMenuItems: exported - '" + entry.getValue().getName() + "'");
            }
        }
        //write the properties to the properties file
        try {
            FileOutputStream out = new FileOutputStream(ExportFilePath);
            try {
                MenuItemProps.store(out, util.PropertyComment);
                out.close();
            } catch (IOException ex) {
                System.out.println("ADM: mExportMenuItems: error exporting menus " + util.class.getName() + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ADM: mExportMenuItems: error exporting menus " + util.class.getName() + ex);
        }

        System.out.println("ADM: mExportMenuItems: exported " + MenuNodeList().size() + " MenuItems");
        
        return;
    }
    
    private static void PropertyAdd(Properties inProp, String Location, String Setting){
        if (Setting!=null){
            inProp.setProperty(Location, Setting);
        }
    }
    
    private static void AddNode(MenuNode aNode){
        if (!NodeExists(root(), aNode.Name)){
            //check if the current nodes parent exists yet
            if (aNode.Parent.equals(util.TopMenu)){
                //root.add(new DefaultMutableTreeNode(aNode));
                InsertNode(root(), aNode, Boolean.FALSE);
                //System.out.println("ADM: mAddNode: node '" + aNode.ButtonText + "' not found so adding to ROOT");
            }else{
                AddNode(MenuNodeList().get(aNode.Parent));
                DefaultMutableTreeNode tParent = FindNode(root(), aNode.Parent);
                //tParent.add(new DefaultMutableTreeNode(aNode));
                InsertNode(tParent, aNode, Boolean.FALSE);
                //System.out.println("ADM: mAddNode: node '" + aNode.ButtonText + "' not found so adding");
            }
        }else{
            //System.out.println("ADM: mAddNode: node '" + aNode.ButtonText + "' already exists");
        }
    }
    
    private static void InsertNode(DefaultMutableTreeNode iParent, MenuNode iNode, Boolean FixSort){
        //insert the node according to the SortKey value
        if ( iParent.getChildCount() == 0 || iNode.SortKey < 0 ) {
            //no children or forced to bottom (by -1 SortKey) so just do an add
            InsertNode(iParent,iNode,-1);
        }else{
            
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
                    if (( iNode.SortKey >= lastChild.SortKey ) && ( iNode.SortKey < nextChild.SortKey ) ){
                        // Ok it needs to go between these two
                        InsertNode(iParent,iNode,i);
                        done = Boolean.TRUE;
                    }
                }
                if ( !done ) { // didn't find a place to insert the node must be the last one
                    InsertNode(iParent,iNode,iParent.getChildCount());
                }
            }            
        }
        if (FixSort){
            //fix the sortkeys when a single Insert calls this function
            SortKeyUpdate(iParent);
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
    
    @SuppressWarnings("unchecked")
    public static DefaultMutableTreeNode FindNode(DefaultMutableTreeNode Root, DefaultMutableTreeNode Node){
        Enumeration<DefaultMutableTreeNode> en = Root.preorderEnumeration();
        while (en.hasMoreElements())   {
            DefaultMutableTreeNode child = en.nextElement();
            if(child.equals(Node)) 
            { 
                //tree node with string found 
                //System.out.println("ADM: mFindNode: '" + Node + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return child;                          
            } 
        }         
        //System.out.println("ADM: mFindNode: '" + Node + "' not found.");
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
                //System.out.println("ADM: mFindNode: '" + NodeKey + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return child;                          
            } 
        }         
        //System.out.println("ADM: mFindNode: '" + NodeKey + "' not found.");
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
                //System.out.println("ADM: mNodeExists: '" + NodeKey + "' found = '" + child + "' childcount = '" + child.getChildCount() + "' Parent = '" + child.getParent() + "' Level = '" + child.getLevel() + "' Leaf = '" + child.isLeaf() + "'"  );
                return Boolean.TRUE;                          
            } 
        }         
        //System.out.println("ADM: mNodeExists: '" + NodeKey + "' not found.");
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
        System.out.println("ADM: msetParent: node = '" + Node + "' oldParent = '" + oldParent +"' newParent = '" + newParent + "'");
    }
    
    private static void Save(String Name, String PropType, String Setting){
        if (!Name.equals(util.TopMenu)){
            String PropLocation = util.SagePropertyLocation + Name;
            util.SetProperty(PropLocation + "/" + PropType, Setting);
            //now save the specifc node field change
            if (PropType.equals("Action")){
                MenuNodeList().get(Name).ActionAttribute = Setting;
            }else if (PropType.equals("ActionType")){
                MenuNodeList().get(Name).ActionType = Setting;
            }else if (PropType.equals("BGImageFile")){
                MenuNodeList().get(Name).BGImageFile = Setting;
            }else if (PropType.equals("ButtonText")){
                MenuNodeList().get(Name).ButtonText = Setting;
            }else if (PropType.equals("IsActive")){
                try {
                    MenuNodeList().get(Name).IsActive = util.TriState.valueOf(Setting);
                } catch (Exception e) {
                    MenuNodeList().get(Name).IsActive = util.TriState.YES;
                }
            }else if (PropType.equals("IsDefault")){
                MenuNodeList().get(Name).IsDefault = Boolean.parseBoolean(Setting);
            }else if (PropType.equals("SubMenu")){
                MenuNodeList().get(Name).SubMenu = Setting;
            }else if (PropType.equals("Parent")){
                MenuNodeList().get(Name).Parent = Setting;
            }else if (PropType.equals("BlockedSageUsersList")){
                //assume that the list has already been modified by the calling routine
            }else{
                System.out.println("ADM: mSave - invalid option passed for '" + PropType + "' '" + Name + "' = '" + Setting + "'");
            }
            //System.out.println("ADM: mSave completed for '" + PropType + "' '" + Name + "' = '" + Setting + "'");
        }
    }
    
    public static String GetNewMenuItemName(){
        Boolean UniqueName = Boolean.FALSE;
        String NewName = null;
        while (!UniqueName){
            NewName = util.GenerateRandomadmName();
            //check to see that the name is unique from other existing MenuItemNames
            UniqueName = !MenuNodeList().containsKey(NewName);
        }
        return NewName;
    }

    @SuppressWarnings("unchecked")
    public static Map<String,MenuNode> MenuNodeList(){
        String UIContext = sagex.api.Global.GetUIContextName();
        if (!UIMenuNodeList.containsKey(UIContext)){
            //create the MenuNodeList for this UIContext
            System.out.println("ADM: mMenuNodeList: creating MenuNodeList for '" + UIContext + "'");
            UIMenuNodeList.put(UIContext, new LinkedHashMap<String,MenuNode>());
        }
        //System.out.println("ADM: mMenuNodeList: '" + UIContext + "'");
        Map<String,MenuNode> tMenuNodeList = null;
        try {
            tMenuNodeList = UIMenuNodeList.get(UIContext);
        } catch (Exception e) {
            //System.out.println("ADM: mMenuNodeList ERROR: '" + UIContext + "' = '" + e + "'");
        }
        return tMenuNodeList;
    }

    public static DefaultMutableTreeNode root(){
        String UIContext = sagex.api.Global.GetUIContextName();
        if (!UIroot.containsKey(UIContext)){
            System.out.println("ADM: mroot: creating root for '" + UIContext + "'");
            MenuNode rootNode = new MenuNode(util.TopMenu);
            UIroot.put(UIContext,new DefaultMutableTreeNode(rootNode));
            rootNode.NodeItem = UIroot.get(UIContext);
            rootNode.ButtonText = "Top Level";
        }
        //System.out.println("ADM: mroot: '" + UIContext + "'");
        return UIroot.get(UIContext);
    }
    
}