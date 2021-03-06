package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.helperObjects.AppProperties;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.RdfRepresentation;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ValidationRequest;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class MainController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private AppProperties appProperties;

    @GetMapping("/")
    public String index(Model model) {
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info("Index!");
        Map<String,String> models = this.indexService.getAllModels();
        model.addAttribute("models", models);
        return "index";
    }

    @RequestMapping(value={"/model"}, method=RequestMethod.GET)
    public ModelAndView  indexModel(@RequestParam("uri") String uri) {
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info("Searching for: " + uri);

        ModelAndView mav = new ModelAndView();
        RdfRepresentation rdfObject = this.indexService.getObjectForUri(uri);

        if (rdfObject instanceof nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) {
            nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model myModel = (nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model) rdfObject;
            mav.addObject("model", myModel);

            if (appProperties.isValidationEnabled()) {
                List<ValidationRequest> validationRequests = validationService.getValidationRequestsForModel(myModel);
                mav.addObject("validationRequests", validationRequests);
            }
            mav.addObject("validationEnabled", appProperties.isValidationEnabled());
            mav.setViewName("model");
            return mav;
        }

        mav.setViewName("model_not_found");
        mav.addObject("modelURI", uri);
        return mav;
    }

}
