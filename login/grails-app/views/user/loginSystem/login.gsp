<html >
     <head >
         <meta  http-equiv ="Content-Type"  content ="text/html; charset=UTF-8"/>
         <meta  name ="layout"  content ="main"   />
         <title > Login Page </title >
     </head >
     <body >
         <div  class ="nav" >
             <span  class ="menuButton" ><a  class ="home" href ="${createLinkTo(dir:'')}" > Home </a >
             </span >
         </div >
         < div  class ="body" >
             < h1 >
                Login Page
             </h1 >
             <g:if  test ="${message }" >
                 <div  class ="message" >
                    ${message }
                 </div >
             </g:if >
             <g:form  action ="loginSystem"  method ="post" >
                <div  class ="dialog" >
                    <table >
                        <tbody >
                            <tr  class ='prop' >
                                <td  valign ='top'  class ='name' >
                                    <label  for ='username' >
                                       Username:
                                    </label >
                                </td >
                                <td  valign ='top'  class ='value  ${hasErrors(bean:user,field:'username','errors')}' >
                                                     <input  type ="text"  id ='username'  name ='username'
                                                         value ="${user?.username?.encodeAsHTML()}"   />
                                                 </td >
                            </tr >
                            <tr  class ='prop' >
                                 <td  valign ='top'  class ='name' >
                                     <label  for ='password' >
                                        Password:
                                     </label >
                                 </td >
                                 <td  valign ='top'  class ='value  ${hasErrors(bean:user,field:'password','errors')}' >
                                     <input  type ="text"  id ='password'  name ='password'
                                         value ="${user?.password?.encodeAsHTML()}"   />
                                 </td >
                             </tr >
                         </tbody >
                     </table >
                 </div >
                 <div  class ="buttons" >
                     <span  class ="button" ><g:submitButton  class ="save" name ="login"  value ="Login" ></g:submitButton >
                     </span >
                 </div >
                 <g:link  action ="loginSystem"  event ="register" > Register </g:link >
             </g:form >
         </div >
     </body >
</html >