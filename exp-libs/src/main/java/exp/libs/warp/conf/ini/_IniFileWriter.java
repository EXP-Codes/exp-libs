/*
 * Java INI Package
 * Copyright (C) 2008 David Lewis
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public Licence as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public Licence for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package exp.libs.warp.conf.ini;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dtools.ini.Commentable;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.IniUtilities;

/**
 * The IniFileWriter class is a simple class which allows the user to write an
 * IniFile object to the hard disk. One of the class' greatest advantage is that
 * it removes the complexity of writing files from the user.   
 * 
 * 使得 ini文件可以支持除了 ASCII 以外的字符编码.
 * 
 * 注：
 * javaini-1.1.0.0.jar 包内的 MANIFEST.MF 文件强制声明了 Seal（加封）安全机制:
 * Sealed: true （只能引用同jar下的类）
 * 因此不能通过覆写类路径的方式修改 org.dtools.ini.IniFileWriter 的内容
 * 
 * @author David Lewis
 * @version 0.3.0
 * @since 0.1.14
 */
class _IniFileWriter {

    /**
     * The text encoding for writing String to files.
     * @since 0.1.16
     */
    public static final String ENCODING = "ASCII";
    
    /**
     * The <code>IniFile<code> to write to the hard disk. 
     */
    private IniFile ini;
    
    /**
     * The file where to write the <code>IniFile</code> to
     */
    private File file;
    
    private String encoding;
    
    /**
     * Boolean value that, if true, adds a separate line before and after each
     * section.
     * 
     * @since 0.3.0
     */
    private boolean sectionLineSeparator;
    
    /**
     * Boolean value that, if true, adds a space character before and after the
     * equals character (<code>'='</code>) in an item.
     * 
     * @since 0.3.0
     */
    private boolean includeSpaces;
    
    /**
     * Boolean value that, if true, adds a separate line between each item.
     * 
     * @since 0.3.0
     */
    private boolean itemLineSeparator;

    public _IniFileWriter( IniFile ini, File file ) {
    	this(ini, file, null);
    }
    
    /**
     * Creates a new IniFileWriter thread instance.
     * 
     * @param ini The IniFile to write.
     * @param file The File where to write the IniFile to.
     * 
     * @since 0.1.14
     */
    public _IniFileWriter( IniFile ini, File file, String encoding ) {

        if( ini == null ) {
            throw new IllegalArgumentException( "Cannot write a null IniFile" );
        }
        if( file == null ) {
            throw new IllegalArgumentException( 
                    "Cannot write an IniFile to a null file" );
        }
        
        this.ini = ini;
        this.file = file;
        this.encoding = (encoding == null || "".equals(encoding) ? 
        		ENCODING : encoding);
        
        // set parameters of IniFileWriter object
        setIncludeSpaces( false );
        setItemLineSeparator( false );
        setSectionLineSeparator( false );
    }
    
    /**
     * This method converts an INI file to a String object.
     * 
     * @param ini The IniFile to convert.
     * @return A String that represents the IniFile.
     * 
     * @since 0.1.14
     */
    private String iniToString( IniFile ini ) {
        
        // declare and initialise
        StringBuilder builder = new StringBuilder();

        //**********************************************************************
        // Loop through all the sections and append section to ini file
        //**********************************************************************
        
        int size = ini.getNumberOfSections();
        
        for( int i=0; i<size; i++ ) {
            IniSection section = ini.getSection(i);
            
            builder.append( sectionToString(section) );
            builder.append( IniUtilities.NEW_LINE );
        }
        
        // return text
        return builder.toString();
    }
    
    /**
     * <p>This method takes a pre or post comment from either an item or
     * section, and converts the comment to the correct format for a text file.
     * This is mainly for comments that are on multiple lines.</p>
     * 
     * @param comment The comment to format.
     * @param prefixNewLine If true, the new-line character is added before the
     *        comment (as in the case for post-comments). Otherwise, the
     *        new-line character is added after the comment (as in the case of
     *        pre-comments).
     * @return The comment in the correct format for a test file.
     * @since 0.3.0
     */
    private String formatComment( String comment, boolean prefixNewLine ) {
        
        StringBuilder sb = new StringBuilder();
        
        // if the comment is multiple lines, then split it and add each line
        // separately
        if( comment.contains( "\n" ) ) {
            
            String[] comments = comment.split( "\n" );
            
            for( String aComment : comments ) {
                
                if( prefixNewLine )
                    sb.append( IniUtilities.NEW_LINE );
                
                sb.append( Commentable.COMMENT_SYMBOL + aComment );
                
                if( !prefixNewLine )
                    sb.append( IniUtilities.NEW_LINE );
            }
        }
        else {
            if( prefixNewLine )
                sb.append( IniUtilities.NEW_LINE );
            
            sb.append( Commentable.COMMENT_SYMBOL + comment );
            
            if( !prefixNewLine )
                sb.append( IniUtilities.NEW_LINE );
        }
        
        return sb.toString();
    }
    
    /**
     * This method converts an IniItem to a String object.
     * 
     * @param item The IniItem to convert.
     * @return A String that best represents the IniItem
     * 
     * @since 0.1.14
     */
    private String itemToString(IniItem item) {

        //**********************************************************************
        // Declarations and initialisations
        //**********************************************************************
        String comment;
        StringBuilder builder = new StringBuilder();
        
        //**********************************************************************
        // if there is a pre-comment then add it
        //**********************************************************************
        comment = item.getPreComment();
        
        if( !comment.equals("") ) {
            builder.append( formatComment(comment,false) );
        }

        //**********************************************************************
        // add item
        //**********************************************************************
        if( includeSpaces ) {
            builder.append( item.getName() + " = " );
        }
        else {
            builder.append( item.getName() + "=" );
        }
        
        if( item.getValue() != null ) {
            builder.append( item.getValue() );
        }
        
        // if there is an end line comment, then add it
        if( !item.getEndLineComment().equals("") ) {
            builder.append( " " +
                    Commentable.COMMENT_SYMBOL + item.getEndLineComment() );
        }

        //**********************************************************************
        // if there is a post comment, then add it
        //**********************************************************************
        comment = item.getPostComment();

        if( !comment.equals("") ) {
            builder.append( formatComment(comment,true) );
            builder.append( IniUtilities.NEW_LINE );
        }
        else if( itemLineSeparator ) {
            builder.append( IniUtilities.NEW_LINE );
        }

        //**********************************************************************
        // return text
        //**********************************************************************
        return builder.toString();
    }
    
    /**
     * This method converts an IniSection object to a String object.
     * 
     * @param section The IniSection object to convert.
     * @return A String object that best represents the IniSection.
     * 
     * @since 0.1.14
     */
    private String sectionToString( IniSection section ) {

        //**********************************************************************
        // Declarations and initialisations
        //**********************************************************************
        String comment;
        StringBuilder builder = new StringBuilder();

        //**********************************************************************
        // check to see if sectionLineSeparator is set to true
        //**********************************************************************
        if( sectionLineSeparator ) {
            builder.append( IniUtilities.NEW_LINE );
        }
        
        //**********************************************************************
        // add pre-comment if one exists
        //**********************************************************************
        comment = section.getPreComment();
        
        if( !comment.equals("") ) {
            builder.append( formatComment(comment,false) );
        }

        //**********************************************************************
        // add section heading
        //**********************************************************************
        builder.append( "[" + section.getName() + "]" );

        //**********************************************************************
        // add end line comment
        //**********************************************************************
        comment = section.getEndLineComment();
        
        if( !comment.equals("") ) {
            builder.append( " " + Commentable.COMMENT_SYMBOL + comment );
        }

        //**********************************************************************
        // add post comment
        //**********************************************************************
        comment = section.getPostComment();

        if( !comment.equals("") ) {
            builder.append( formatComment(comment,true) );
            builder.append( IniUtilities.NEW_LINE );
        }
        else if( sectionLineSeparator ) {
            builder.append( IniUtilities.NEW_LINE );
        }
        
        //**********************************************************************
        // Loop through all the items and append section to ini file
        //**********************************************************************
        int size = section.getNumberOfItems();
        
        for( int i=0; i<size; i++ ) {
            IniItem item = section.getItem(i);
            builder.append( IniUtilities.NEW_LINE );
            builder.append( itemToString(item) );
        }

        //**********************************************************************
        // return text
        //**********************************************************************
        return builder.toString();
    }
    
    /**
     * This method sets whether a space character should be placed before and
     * after the equals character in an item.
     * 
     * @param value If true, a space character is placed before and after the
     *        equals character.
     *        
     * @since 0.3.0
     */
    public void setIncludeSpaces( boolean value ) {
        includeSpaces = value;
    }

    /**
     * This method sets whether an empty line should be included before and
     * after an item has occurred in the file.
     * 
     * @param value If true, an empty line will be included before and after an
     *        item.
     * @since 0.3.0
     */
    public void setItemLineSeparator( boolean value ) {
        itemLineSeparator = value;
    }
    
    /**
     * This method sets whether an empty line should be included before and
     * after a section has occurred in the file.
     * 
     * @param value If true, an empty line will be included before and after a
     *        section.
     * @since 0.3.0
     */
    public void setSectionLineSeparator( boolean value ) {
        sectionLineSeparator = value;
    }

    /**
     * <p>This method actually writes the <code>IniFile</code> to the File 
     * object (both of which are given in the constructor).</p> 
     * 
     * @throws IOException If an I\O exception occurs.
     * @since 0.1.14
     */
    public void write() throws IOException {
        BufferedWriter bufferWriter = null;
        
        // do not use FileWriter as you cannot manually set the character
        // encoding
        
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, encoding );
        bufferWriter = new BufferedWriter( osw );
        
        bufferWriter.write( iniToString(ini) );
        bufferWriter.close();
    }
}
