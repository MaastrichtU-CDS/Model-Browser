package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.dto.SearchOptions;
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
    Logger logger = Logger.getLogger(this.getClass().toString());

    @GetMapping("/")
    public String index(Model model) {
        logger.info("Index!");
        Map<String,String> models = this.indexService.getAllModels();
        model.addAttribute("models", models);
        model.addAttribute("searchOptions", new SearchOptions());
        return "index";
    }

    @RequestMapping(value={"/model"}, method=RequestMethod.GET)
    public ModelAndView  indexModel(@RequestParam("uri") String uri) {
        logger.info("Searching for: " + uri);

        ModelAndView mav = new ModelAndView();
        RdfRepresentation rdfObject = this.indexService.getObjectForUri(uri);

        if (rdfObject instanceof nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) {
            mav.addObject("model", (nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) rdfObject);
            mav.setViewName("model");
            return mav;
        }

        mav.setViewName("model_not_found");
        mav.addObject("modelURI", uri);
        return mav;
    }

    @RequestMapping(value="/basicSearch", method=RequestMethod.POST)
    public ModelAndView searchModel(@ModelAttribute("searchOptions") SearchOptions searchOptions) {
        logger.info("searching " + searchOptions.getSearchString());

        return new ModelAndView("index");
    }

}
