package org.communis.spring.entity.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("${entity.generator.endpoint}")
public class EntityController {

    @Autowired
    private EntityStorage entityStorage;

    @Autowired
    private EntityGenerator entityGenerator;

    @GetMapping("/list")
    @ResponseBody
    public List<String> list() {
        return entityStorage.getAvailableNames();
    }

    @PostMapping("/generate/{name}")
    @ResponseBody
    public Object fake(@PathVariable("name") String name) {
        return entityGenerator.generate(name);
    }
}
