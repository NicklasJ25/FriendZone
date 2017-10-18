using FriendZoneAPI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class UserController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/user
        public List<User> Get()
        {
            List<User> users = database.Users.ToList();
            return users;
        }

        // GET api/user/email
        public User Get(string email)
        {
            User user = database.Users.Include(u => u.User2).SingleOrDefault(u => u.Email.Equals(email));
            user.User1 = null;
            user.User2.User1 = null;
            user.User2.User2 = null;
            return user;
        }

        // POST api/user
        public void Post([FromBody]User user)
        {
            database.Users.Add(user);
            database.SaveChanges();
        }

        // PUT api/user/email
        public void Put(string email, [FromBody]User newUser)
        {
            User oldUser = database.Users.Find(email);
            oldUser.Email = newUser.Email;
            oldUser.Firstname = newUser.Firstname;
            oldUser.Lastname = newUser.Lastname;
            oldUser.Birthday = newUser.Birthday;
            oldUser.Phone = newUser.Phone;
            oldUser.Streetname = newUser.Streetname;
            oldUser.Postalcode = newUser.Postalcode;
            oldUser.ProfilePicture = newUser.ProfilePicture;
            oldUser.Password = newUser.Password;
            oldUser.Partner = newUser.Partner;
            database.SaveChanges();
        }

        // DELETE api/user/email
        public void Delete(string email)
        {
            database.Users.Remove(database.Users.SingleOrDefault(u => u.Email.Equals(email)));
            database.SaveChanges();
        }
    }
}
