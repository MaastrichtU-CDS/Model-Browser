package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.RdfRepresentation;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    public ModelAndView  indexModel(HttpServletRequest request) {
        Logger logger = Logger.getLogger(this.getClass().toString());

        String modelURI = request.getRequestURI().replaceFirst("/model/", "");

        ModelAndView mav = new ModelAndView();
        RdfRepresentation rdfObject = this.indexService.getObjectForUri(modelURI);

        if (rdfObject instanceof nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) {
            mav.addObject("model", (nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) rdfObject);
            mav.setViewName("model");
            return mav;
        }

        mav.setViewName("model_not_found");
        mav.addObject("modelURI", modelURI);
        return mav;
    }

}
