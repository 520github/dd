package me.twocoffee.service.fetch.video;

import java.net.URI;

/**
 * A playlist element.
 *
 * @author dkuffner
 */
public interface Element {

    public String getTitle();


    public int getDuration();

    /**
     * URI to media or playlist.
     *
     * @return the URI.
     */
    public URI getURI();

    /**
     * Media can be encrypted.
     *
     * @return true if media encrypted.
     */
    public boolean isEncrypted();

    /**
     * Element can be another playlist.
     *
     * @return true if element a playlist.
     */
    public boolean isPlayList();

    /**
     * Element is a media file.
     *
     * @return true if element a media file and not a playlist.
     */
    public boolean isMedia();

    /**
     * The program date.
     *
     * @return -1 in case of program date is not set.
     */
    public long getProgramDate();

}
