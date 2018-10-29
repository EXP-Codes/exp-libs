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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.dtools.ini.Commentable;
import org.dtools.ini.FormatException;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.InvalidNameException;

/**
 * <p>This class provides users an easy way to read INI files from the 
 * hard disk and parse them to an <code>IniFile</code> object.<p>
 * 
 * 使得 ini文件可以支持除了 ASCII 以外的字符编码.
 * 
 * 注：
 * javaini-1.1.0.0.jar 包内的 MANIFEST.MF 文件强制声明了 Seal（加封）安全机制:
 * Sealed: true （只能引用同jar下的类）
 * 因此不能通过覆写类路径的方式修改 org.dtools.ini.IniFileReader 的内容
 * 
 * @author David Lewis
 * @version 1.1.0
 * @since 0.1.14
 */
class _IniFileReader {
    
    /**
     * <p>This method returns the end line comment from the given string. For
     * the given string to have an end line comment, the string must be either a
     * valid section (determined by <code>boolean
     * <strong>isSection</strong>(String)</code>) or a valid item (determined by
     * <code>boolean <strong>isItem</strong>(String)</code>). If the given
     * string is nether a section or an item then an exception is thrown.</p>
     * 
     * @param line The line to get the end line comment from.
     * @return The end line comment from the given string (minus the comment
     * symbol), or an empty string ("") if no comment exists.
     * @throws FormatException If the given string is neither a section nor an
     * item.
     * @see #isItem(String)
     * @see #isSection(String)
     * @since 0.1.14
     */
    static String getEndLineComment( String line ) {
        
        if( !isSection(line) && !isItem(line) ) {
            throw new FormatException( "getEndLineComment(String) is unable to " + 
                    "return the comment from the given string (\"" +
                    line + "\" as it is not an item nor a section." );
        }
        
        int pos = line.indexOf( Commentable.COMMENT_SYMBOL );
        
        if( pos == -1 ) {
            return "";
        }
        else {
            return line.substring( pos+1 ).trim();
        }
    }

    /**
     * <p>This method returns the name of the item in the given string. For an
     * item name to be returned the given string must be a valid item
     * (determined by <code>boolean <strong>isItem</strong>(String)</code>). If
     * the given string is not an item then an exception is thrown.</p>
     * 
     * @param line The line to get the item name from.
     * @return The name of the item from the given string.
     * @throws FormatException If the given string is not an item.
     * @see #isItem(String)
     * @since 0.1.16
     */
    static String getItemName( String line ) {
        
        if( !isItem(line) ) {
            throw new FormatException( "getItemName(String) is unable to " + 
                    "return the name of the item as the given string (\"" +
                    line + "\" is not an item." );
        }
        
        // get the index of the first occurrence of the equals sign
        int pos = line.indexOf( '=' );
        
        // no occurrence of equals sign
        if( pos == -1 ) {
            return "";
        }
        else {
            return line.substring( 0, pos ).trim();
        }
    }

    /**
     * <p>This method returns the value of the item in the given string. For an
     * item value to be returned the given string must be a valid item
     * (determined by <code>boolean <strong>isItem</strong>(String)</code>). If
     * the given string is not an item then an exception is thrown.</p>
     * 
     * @param line The line to get the item value from.
     * @return The value of the item from the given string.
     * @throws FormatException If the given string is not an item.
     * @see #isItem(String)
     * @since 0.1.16
     */
    static String getItemValue( String line ) {
        
        if( !isItem(line) ) {
            throw new FormatException( "getItemValue(String) is unable to " + 
                    "return the value of the item as the given string (\"" +
                    line + "\" is not an item." );
        }
        
        //**********************************************************************
        // get the index of the first occurrence of the equals sign and the
        // comment sign
        //**********************************************************************
        int posEquals = line.indexOf( '=' );
        int posComment = line.indexOf( Commentable.COMMENT_SYMBOL );
        
        // no occurrence of equals sign
        if( posEquals == -1 ) {
            
            // no occurence of comment sign
            if( posComment == -1 ) {
                return line;
            }
            else {
                return line.substring(0, posComment ).trim();
            }
        }
        else {
            
            // no occurrence of comment sign
            if( posComment == -1 ) {
                return line.substring(posEquals+1).trim();
            }
            else {
                return line.substring(posEquals+1, posComment).trim();
            }
        }
    }

    /**
     * <p>This method returns the name of the section in the given string. For a
     * section name to be returned the given string must be a valid section
     * (determined by <code>boolean <strong>isSection</strong>(String)</code>).
     * If the given string is not an item then an exception is thrown.</p>
     * 
     * @param line The line to get the item name from.
     * @return The name of the item from the given string.
     * @throws FormatException if the given string is not an item.
     * @see #isSection(String)
     * @since 0.1.16
     */
    static String getSectionName( String line ) {
        
        if( !isSection(line) ) {
            throw new FormatException( "getSectionName(String) is unable to " + 
                    "return the name of the section as the given string (\"" +
                    line + "\" is not a section." );
        }
        
        int firstPos = line.indexOf( '[' );
        int lastPos = line.indexOf( ']' );
        
        return line.substring(firstPos+1, lastPos).trim();
    }
    
    /**
     * <p>Predicate that returns true if the entire given string contains only
     * a comment. For a string to contain only a comment, the first character 
     * (after any white space) must be a comment symbol (as defined by 
     * Commentable.<em>COMMENT_SYMBOL</em>).</p>
     * 
     * @param line The line of text to test.
     * @return True if the given string contains only a comment, false
     * otherwise.
     * @see Commentable#COMMENT_SYMBOL
     * @since 0.1.14
     */
    static boolean isComment( String line ) {
        
        line = line.trim();
        
        if( line.isEmpty() ) {
            return false;
        }
        else {
            // if the line is not empty, then return true only if the first
            // character is a comment symbol.
            char firstChar = line.charAt(0);
            return firstChar == Commentable.COMMENT_SYMBOL;
        }
    }
    
    /**
     * <p>Predicate that returns true if the given string contains an item. For
     * a string to contain an item, the string should be in the following
     * format:</p>
     * <p>
     * <code><em>item_name</em> <strong>"="</strong> [<em>item_value</em>]
     * [<em>end_line_comment</em>]</code>
     * </p>
     * <p>where:<p>
     * <ul>
     *   <li><em>item_name</em> - the name of the item.
     *   <li><em>item_value</em> - (optional) the value of the item.
     *   <li><em>end_line_comment</em> - (optional) a comment
     * </ul>
     * <p>However this method only requires that the string has at least a name
     * followed by an equals sign. Also the name of the section does not have to
     * be a valid name, as defined by the <code>IniFile</code>'s
     * <code>IniValidator</code>, as that is the responsibility of the object
     * <code>IniValidator</code>.</p>
     * 
     * @param line The string to test.
     * @return True if the string is a valid item, false otherwise.
     * @since 0.1.14
     */
    static boolean isItem(String line) {
        
        line = removeComments(line);
        
        if( line.isEmpty() ) {
            return false;
        }
        else {
            int pos = line.indexOf( '=' );
            
            if( pos != -1 ) {
                
                String name = line.substring(0,pos).trim();
                
                return (name.length() > 0);
            }
            else {
                return false;
            }
        }
    }
    
    /**
     * <p>Predicate that returns true if the given string contains a section.
     * For a string to contain a section, the string should be in the following
     * format:</p>
     * <p>
     * <code><strong>"["</strong><em>sectionm_name</em><strong>"]"</strong>
     * [<em>end_line_comment</em>]</code>
     * </p>
     * <p>where:<p>
     * <ul>
     *   <li><em>sectionm_name</em> - the name of the section, which
     *   <strong>MUST</strong> be surrounded by square brackets (i.e. [ and ] ).
     *   <li><em>end_line_comment</em> - (optional) a comment
     * </ul>
     * <p>However this method only requires that the string has at least a name
     * followed by an equals sign. Also the name of the section does not have to
     * be a valid name, as defined by the <code>IniFile</code>'s
     * <code>IniValidator</code>, as that is the responsibility of the object
     * <code>IniValidator</code>.<p>
     * 
     * @param line The string to test.
     * @return True if the string is a valid section, false otherwise.
     * @since 0.1.14
     */
    static boolean isSection( String line ) {
        
        line = removeComments(line);
        
        if( line.isEmpty() ) {
            return false;
        }
        else {
            char firstChar = line.charAt(0);
            char lastChar  = line.charAt( line.length()-1 );
            
            return firstChar == '[' && lastChar == ']';
        }
    }
    
    /**
     * <p>This method removes any comments (and comment symbols) from the given
     * string and returns the remaining string. This allows other methods to
     * test a string without concerning themselves about any comments within the
     * string (e.g. {@link #isItem(String)} and {@link #isSection(String)} ).
     * </p>
     * 
     * @param line The string that will have comments removed from it.
     * @return same as the imput string minus any comments.
     * @since 0.1.14
     */
    static String removeComments( String line ) {
        
        if( line.contains(String.valueOf(Commentable.COMMENT_SYMBOL)) ) {
            return line.substring(0, line.indexOf(Commentable.COMMENT_SYMBOL) ).trim();
        }
        else {
            return line.trim();
        }
    }
    
    /**
     * A reference to the file which will be read in as an INI file.
     * 
     * @since 0.1.14
     */
    private File file;
    
    /**
     * A reference to the <code>IniFile</code> object which will contian the 
     * parsed data from the INI file.
     * 
     * @since 0.1.14
     */
    private IniFile ini;
    
    private String encoding;
    
    public _IniFileReader( IniFile ini, File file ) {
    	this(ini, file, null);
    }
    
    /**
     * Default constructor, creates a new <code>IniFileReader</code> object
     * which will read from the given file and populate the given data from the
     * file into the given <code>IniFile</code> object.</p>
     * 
     * @param ini The IniFile which will be populated.
     * @param file The file that will be read as an INI file.
     * @since 0.1.14
     */
    public _IniFileReader( IniFile ini, File file, String encoding ) {

        //**********************************************************************
        // Step 1 - Check that neither of the arguments are null
        //**********************************************************************
        if( ini == null ) {
            throw new NullPointerException(
                    "The given IniFile cannot be null." );
        }
        
        if( file == null ) {
            throw new NullPointerException( "The given File cannot be null." );
        }

        //**********************************************************************
        // Step 2 - set the fields of this object.
        //**********************************************************************
        
        this.file = file;
        this.ini = ini;
        this.encoding = (encoding == null || "".equals(encoding) ? 
        		_IniFileWriter.ENCODING : encoding);
    }
    
    /**
     * <p>This method begins the reading process of the file. The method opens
     * the input file, which is set in the constructor, reads each line from the
     * file, and parse that file to the <code>IniFile</code> object, which is
     * also set in the constructor.</p>
     * 
     * <p>If the method encounters a line of text from the string which it is 
     * unable to parse to the <code>IniFile</code>, then this method throws a 
     * <code>FormatException</code> exception.</p>
     * 
     * @throws FormatException If an error is encountered whilst reading the
     * input file.
     * @throws IOException If an exception occurs whilst reading the file.
     * @since 0.1.14
     */
    @SuppressWarnings("resource")
	public void read() throws IOException {
        
        BufferedReader reader;
        String line;
        IniSection currentSection = null;
            
        //**********************************************************************
        // set up reader to read input file
        //**********************************************************************
        reader = new BufferedReader(
              new InputStreamReader(
                      new FileInputStream(file), encoding )
        );

        //**********************************************************************
        // process each line of the text file
        //**********************************************************************
        String comment = "";
        Commentable lastCommentable = null;
        
        while( (line = reader.readLine()) != null ) {
            
            //******************************************************************
            // Trim any excess space from the beginning and end of the line
            //******************************************************************
            line = line.trim();
            
            //******************************************************************
            // If the line is empty, go to the next line
            //******************************************************************
            if( line.isEmpty() ) {
                
                //**************************************************************
                // add post comment
                //**************************************************************
                if( !comment.isEmpty() && lastCommentable != null ) {
                    lastCommentable.setPostComment( comment );
                    comment = "";
                }
                
                //**************************************************************
                // continue to next line in ini file
                //**************************************************************
                continue;
            }

            //******************************************************************
            // Check to see if it is a comment
            //******************************************************************
            else if( isComment(line) ) {
                
                String tmpComment = line.substring(1).trim();
                
                if( comment.isEmpty() ) {
                    comment = tmpComment;
                }
                else {
                    comment += "\n" + tmpComment;
                }
            }
            
            //******************************************************************
            // if the line is a section, then process it
            //******************************************************************
            else if( isSection(line) ) {
                
                // get the name of the section from the line
                String sectionName = getSectionName( line );
                
                String endLineComment = getEndLineComment( line );
                
                // if section already exists, then get section
                if( ini.hasSection(sectionName) ) {
                    currentSection = ini.getSection( sectionName );
                }
                else {
                    // section doesn't already exists
                    // create a new instance of a section
                    currentSection = ini.addSection( sectionName );
                }
                
                /* set the end line comment of the section
                 * 
                 * NOTE: this may replace any previous end line comment if
                 * the section had already existed.
                 */
                currentSection.setEndLineComment( endLineComment );
                
                //**************************************************************
                // add pre comment
                //**************************************************************
                if( !comment.isEmpty() ) {
                    currentSection.setPreComment( comment );
                    comment = "";
                }
                
                //**************************************************************
                // keep a reference of the latest item so that post comments can
                // be added to it later
                //**************************************************************
                lastCommentable = currentSection;
            }

            //******************************************************************
            // If the line is an item, then process the item
            //******************************************************************
            else if( isItem(line) ) {
                
                //**************************************************************
                // Check that a section has already been read
                //**************************************************************
                if( currentSection == null ) {
                    throw new FormatException( "An Item has been read," +
                            "before any section." );
                }
                
                //**************************************************************
                // get name, value and end line comments of the item
                //**************************************************************
                String itemName = getItemName( line );
                String itemValue = getItemValue( line );
                String endLineComment = getEndLineComment( line );
                
                IniItem item;
                
                // if the current section already has an item with same name
                if( currentSection.hasItem(itemName) ) {
                    item = currentSection.getItem( itemName );
                }
                else {
                    
                    // try to create a new instance of item
                    try {
                        item = currentSection.addItem( itemName );
                    }
                    catch( InvalidNameException e ) {
                        throw new FormatException( "The string \"" + 
                                itemName + "\" is an invalid name for an " +
                                "IniItem." );
                    }
                }

                //**************************************************************
                // add value and end line comments
                //**************************************************************
                item.setValue( itemValue );
                item.setEndLineComment( endLineComment );
                
                //**************************************************************
                // add pre comment
                //**************************************************************
                if( !comment.isEmpty() ) {
                    item.setPreComment( comment );
                    comment = "";
                }
                
                //**************************************************************
                // keep a reference of the latest item so that post comments can
                // be added to it later
                //**************************************************************
                lastCommentable = item;
            }
        } // end reading file
        
        //**********************************************************************
        // if there is comment still unprocessed, then add post comment
        //**********************************************************************
        if( !comment.isEmpty() && lastCommentable != null ) {
            lastCommentable.setPostComment( comment );
            comment = "";
        }

        reader.close();
        
    } // end method read()
}
