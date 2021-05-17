package edu.duke.ece651.risc.shared;

public interface Action {
    /**
     *  Execute this action
     *  */
    public void execute();
    //public void execute(Map map);

    /**
     * Check the rules for this action
     * @return null if valid, error msg String if invalid
     **/

    public String check();

    public String check(Map map);

    /**
     * Get the type of this action
     * @return String of this type representation
     */
    public String getType();
}
