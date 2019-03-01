package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";

    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute  @Valid Menu newMenu,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }


    @RequestMapping(value = "view/{Id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable(value="Id") int menuId) {

//        model.addAttribute(new Cheese());
//        model.addAttribute("category", categoryDao.findOne(catId));

        Menu menu = menuDao.findOne(menuId);

        model.addAttribute("title", menu.getName());
        model.addAttribute("cheeses", menu.getCheeses());
        model.addAttribute("menu", menu);


        return "menu/view";
    }


    @RequestMapping(value = "add-item/{Id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable(value="Id") int menuId) {

//        model.addAttribute(new Cheese());
//        model.addAttribute("category", categoryDao.findOne(catId));

        Menu menu = menuDao.findOne(menuId);


        model.addAttribute("title", "Add item to menu: " + menu.getName());
//        model.addAttribute("cheeses", menu.getCheeses());
//        model.addAttribute("menu", menu);
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", addMenuItemForm);



        return "menu/add-item";
    }

    @RequestMapping(value = "add-item/{Id}", method = RequestMethod.POST)
    public String addItem(@ModelAttribute  @Valid AddMenuItemForm newAddMenuItemForm,
                                     Errors errors, Model model) {
//        Menu menu = menuDao.findOne(menuId);

//        newAddMenuItemForm.getMenu();

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu: " + newAddMenuItemForm.getMenu().getName());
            return "menu/add-item";
        }

        Menu menu = menuDao.findOne(newAddMenuItemForm.getMenuId());
        System.out.println("newAddMenuItemForm.getCheeseId() = " + newAddMenuItemForm.getCheeseId());
        Cheese cheese = cheeseDao.findOne(newAddMenuItemForm.getCheeseId());
        menu.getCheeses().add(cheese);

        menuDao.save(menu);

        model.addAttribute("title", menu.getName());
        model.addAttribute("cheeses", menu.getCheeses());
        model.addAttribute("menu", menu);

        return "redirect:../view/" + menu.getId();
    }
}