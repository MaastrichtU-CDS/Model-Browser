package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String index(Model model) {
        Map<String,String> models = this.indexService.getAllModels();
        model.addAttribute("models", models);
        return "index";
    }

}
