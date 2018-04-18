package controller;

import model.Book;
import model.User;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.book.BookService;
import service.sale.SaleService;
import service.search.SearchService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SaleService saleService;

    @RequestMapping(value = "/user", method = RequestMethod.GET, params = {"!action"})
    public String index()
    {
        return "user";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, params = {"action"})
    public String search(@RequestParam String val, @RequestParam String action, Model model) {
        switch (action){
            case "titles":
                model.addAttribute("searchResult",searchService.searchByTitle(val));
                break;
            case "authors":
                model.addAttribute("searchResult",searchService.searchByAuthor(val));
                break;
            case "genres":
                model.addAttribute("searchResult",searchService.searchByGenre(val));
                break;
        }
        return "user";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, params = "action=sell")
    public String sell(@RequestParam int bid, @RequestParam int quantity, Model model, HttpSession session){
        User user = ((User)session.getAttribute("user"));
        Notification<Integer> notification = saleService.sell(bid,quantity,user.getId());
        if(notification.hasErrors()){
            model.addAttribute("saleResult",notification.getFormattedErrors());
        } else {
            model.addAttribute("saleResult","Sale succesful, price: " + notification.getResult()+"");
        }

        return "user";
    }
}
