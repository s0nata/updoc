/**
 * Set an X Window System drawable where the media player should render its
 * video output. If LibVLC was built without X11 output support, then this has
 * no effects. The specified identifier must correspond to an existing
 * Input/Output class X11 window. Pixmaps are <b>not</b> supported. The caller
 * shall ensure that the X11 server is the same as the one the VLC instance
 * has been configured with. If XVideo is <b>not</b> used, it is assumed that
 * the drawable has the following properties in common with the default X11
 * screen: depth, scan line pad, black pixel. This is a bug.
 *
 * @param p_mi the Media Player
 * @param drawable the ID of the X window
 */
  void libvlc_media_player_set_xwindow(libvlc_media_player_t p_mi, int drawable);