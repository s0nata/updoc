/**
* VirtualConsole constructor
* @param parent parent element
* @param ansiColorMode ANSI_COLOR_OFF: don't process ANSI escapes,
* ANSI_COLOR_ON: translate ANSI escapes into css styles, ANSI_COLOR_STRIP:
* strip out ANSI escape sequences but don't apply styles
* sequences, otherwise just strip them out
*/
public VirtualConsole(Element parent, int ansiColorMode)   {
        RStudioGinjector.INSTANCE.injectMembers(this);
        parent_ = parent;
        ansiColorMode_ = ansiColorMode;
        }