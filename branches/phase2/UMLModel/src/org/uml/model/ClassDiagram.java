package org.uml.model;

import org.uml.model.relations.RelationComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * UML Class Diagrams which can contain class, interface and enum components,
 * along with relations among them.
 *
 * @author Uros
 * @version 1.0
 * @see ComponentBase
 * @see RelationComponent
 */
public class ClassDiagram implements Serializable {

    private String name;
    private HashMap<String, ComponentBase> components; // contains classes, interfaces or enums
    private HashMap<String, RelationComponent> relations;
    //compoments counter:
    int compCounter = 1;
    private HashMap<String, PackageComponent> packages;
    int relationsCounter = 0;
    private List<IComponentDeleteListener> deleteListeners = new ArrayList<>();

    /**
     * Standard ClassDiagram constructor without arguments.
     * <p>
     * Sets name to default value and instantiates components, relations and
     * packages hash maps.
     */
    public ClassDiagram() {
        name = "UML Class Diagram";
        this.components = new HashMap<>();
        this.relations = new HashMap<>();
        this.packages = new HashMap<>();
    }

    public void addDeleteListener(IComponentDeleteListener icdl) {
        deleteListeners.add(icdl);
    }

    /**
     * Adds new ClassDiagramComponent into collection of existing components. If
     * component with that name already exists in this collection, new one will
     * be added with it's name concatenated with current component counter
     * number.
     *
     * @param component to be added to collection
     */
    public void addComponent(ComponentBase component) {
        if (nameExists(component.getName())) {
            component.setName(component.getName() + compCounter);
        }
        compCounter++;
        component.setParentDiagram(this);
        components.put(component.getName(), component);
    }

    /**
     * Adds new RealtionComponent into collection of existing relations. If
     * relationComponent with same name already exists in relations, a counter
     * is concatenated to its name and is then added. The counter is then
     * incremented by one.
     *
     * @param relationComponent that will be added to the collection of
     * relations
     */
    public void addRelation(RelationComponent relationComponent) {
        if (relations.containsKey(relationComponent.getName())) {
            relations.put(relationComponent.getName() + relationsCounter, relationComponent);
            relationsCounter++;
        } else {
            relations.put(relationComponent.getName(), relationComponent);
        }
        System.out.println(relations.toString());
    }

    /**
     * Removes the given component from this diagram's collection of components.
     *
     * @param name of the component to be removed
     */
    public void removeComponent(String name) {
        for (IComponentDeleteListener icdl : deleteListeners) {
            icdl.componentDeleted(components.get(name));
        }
        components.remove(name);
    }

    /**
     * Removes the given relation from this diagram's collection of relations.
     *
     * @param name of the relation to be removed
     */
    public void removeRelation(String name) {
        relations.remove(name);
    }

    public void removeRelationsForAComponent(ComponentBase component) {
        List<RelationComponent> toRemove = new LinkedList<>();
        for (Map.Entry<String, RelationComponent> entry : relations.entrySet()) {
            RelationComponent relation = entry.getValue();
            if (relation.getSource().getName().equals(component.getName()) || relation.getTarget().getName().equals(component.getName())) {
                toRemove.add(relation);
            }
        }
        for(RelationComponent rc : toRemove){
            relations.remove(rc.getName());
        }
    }

    /**
     * Returns the collection of components that this diagram has.
     *
     * @return collection of components
     */
    public HashMap<String, ComponentBase> getComponents() {
        return components;
    }

    /**
     * Returns the collection of this diagram's relations.
     *
     * @return collection of relations
     */
    public HashMap<String, RelationComponent> getRelations() {
        return relations;
    }

    /**
     * Sets the collection of relations that exist in this diagram.
     *
     * @param relations to be set
     */
    public void setRelations(HashMap<String, RelationComponent> relations) {
        this.relations = relations;
    }

    /**
     * Checks if component with provided name exists in collection of
     * components.
     *
     * @param name of the component
     * @return if that component exists in collection
     */
    public boolean nameExists(String name) {
        return components.containsKey(name);
    }

    /**
     * Removes component from collection of components and adds same component
     * with new name into that collection.
     *
     * @param comp - component whose name will be changed
     * @param oldName - old component's name
     */
    public void componentNameChanged(ComponentBase comp, String oldName) {
        components.remove(oldName);
        addComponent(comp);
    }

    /**
     * Returns name of this ClassDiagram
     *
     * @return name of the UML Class Diagram
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this ClassDiagram
     *
     * @param name of UML Class Diagram
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds the PackageComponent into package collection.
     *
     * @param pc package to be added
     */
    public void addPackage(PackageComponent pc) {
        packages.put(pc.getName(), pc);
    }

    /**
     * Removes the package from package collection.
     *
     * @param pc package to be removed
     */
    public void removePackage(PackageComponent pc) {
        packages.remove(pc.getName());
    }

    /**
     * Gets the packages collection of this ClassDiagram.
     *
     * @return packages collection
     */
    public HashMap<String, PackageComponent> getPackages() {
        return packages;
    }
}
