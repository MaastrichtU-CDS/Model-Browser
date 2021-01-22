package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.RdfRepresentation;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.logging.Logger;

@Controller
public class MainController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String index(Model model) {
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info("Index!");
        Map<String,String> models = this.indexService.getAllModels();
        model.addAttribute("models", models);
        return "index";
    }

    @RequestMapping(value={"/model/**"}, method=RequestMethod.GET)
    public ModelAndView  indexModel(Model model) {
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info("in model path");

        ModelAndView mav = new ModelAndView();
        RdfRepresentation rdfObject = this.indexService.getObjectForUri("https://fairmodels.org/models/radiotherapy/stiphout_2011.ttl");

        if (rdfObject instanceof nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) {
            mav.addObject("model", (nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) rdfObject);
            mav.setViewName("model");
            return mav;
        }

        mav.setViewName("index");
        return mav;
    }

}
