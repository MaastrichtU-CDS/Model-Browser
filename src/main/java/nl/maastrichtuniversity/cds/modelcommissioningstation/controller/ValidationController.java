package nl.maastrichtuniversity.cds.modelcommissioningstation.controller;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
@RequestMapping("/validation")
public class ValidationController {
    private Logger logger = Logger.getLogger(this.getClass().toString());
    @Autowired private IndexService indexService;
    @Autowired private ValidationService validationService;

    @RequestMapping(value={"/new"}, method= RequestMethod.GET)
    public ModelAndView requestValidation(@RequestParam("uri") String uri) {
        logger.info("Request to validate model: " + uri);
        validationService.requestValidation(uri);
        return new ModelAndView("redirect:/model?uri=" + uri);
    }
}
